package Business.Threads;

import Model.Beans.*;
import Model.DAOs.ContractDAO;
import Model.DAOs.CourierDAO;
import Model.DAOs.OrdersDAO;
import Model.DAOs.ShippingPropertyDAO;
import Model.Exceptions.NotFoundException;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;

public class CheckingInterestInOrder extends Thread {

    private Orders order;
    private OrdersDAO oD;
    private CourierDAO courierDAO;

    public CheckingInterestInOrder(Orders o, OrdersDAO oD) {
        this.order = o;
        this.oD = oD;
        this.courierDAO = new CourierDAO(oD.getDs());
    }

    public void run() {
        //Effettuare il check periodico ogni 5 minuti

        ArrayList<Interest> interests = null;

        boolean flag = true;

        do {
            System.out.println("Thread avviato sull'ordine con id '"+order.getId()+"'... aspetto 5 minuti");
            try {
                Thread.sleep(300*1000);
            } catch (InterruptedException e) {
                System.out.println("Unexpected interrupt received by " + CheckingInterestInOrder.class.getSimpleName() + " associated Thread... printing stack trace and returning");
                e.printStackTrace();
                return;
            }

            try {
                interests = oD.retrieveInterestedCouriers(order.getId());
                flag = false;
            } catch (NotFoundException e) {
                ;
            } catch (Exception e) {
                System.out.println(e);
                e.printStackTrace();
                return;
            }

        } while (flag);

        ShippingPropertyDAO cD = new ShippingPropertyDAO(oD.getDs());
        ContractDAO coD = new ContractDAO(oD.getDs());


        ArrayList<ShippingProperty> orderProperties = order.getOrderProperties();


        int counter = 0;
        int courierIndexToExtract = 0;

        for (int i = 0; i < interests.size(); i++) {

            Courier currCourier = interests.get(i).getCourier();
            try{
                currCourier = courierDAO.retrieve(currCourier.getEmail());
            }catch (Exception e){
                System.out.println("Non Existing Courier with mail '"+currCourier.getEmail()+"' IN DB");
                continue;
            }

            try {

                Contract currContract = coD.retrieve(order.getOrderDisp().getEmail(), currCourier.getEmail());

                ArrayList<ShippingProperty> courierProperties = new ArrayList<>();
                try{
                   courierProperties = cD.agreedProperties(currContract);
                }catch (NotFoundException e){
                    System.out.println("No problem... continue");
                }

                int internCounter = 0;

                for (int j = 0; j < courierProperties.size(); j++) {
                    ShippingProperty sP = courierProperties.get(i);
                    if (orderProperties.contains(sP)) {
                        internCounter++;
                    }
                }

                if (internCounter > counter) {
                    counter = internCounter;
                    courierIndexToExtract = i;
                }

            } catch (NotFoundException e) {
                System.out.println("Fatal error... \nCourier '" + currCourier.getName() + "' and AssignmentDispatcher '" + order.getOrderDisp().getEmail() + "' don't have a contract... removing interest generated");
                //Istruzione per rimuovere l'interessamento di questo corriere a questo ordine...

            } catch (Exception e) {
                System.out.println(e);
                return;
            }

        }

        Interest leveragedInterest = interests.get(courierIndexToExtract);

        Courier courierToAssign = leveragedInterest.getCourier();

        try {
            oD.removeInterestedCouriers(order.getId());
        } catch (Exception e) {
            System.out.println(e);
            e.printStackTrace();
            return;
        }

        try {

            oD.assignToCourier(courierToAssign.getEmail(), order.getId(), new Date(Calendar.getInstance().getTime().getTime()), leveragedInterest.getExpectedDeliveryDate(), leveragedInterest.getTrackingCode());
            System.out.println("Courier '" + courierToAssign.getEmail() + "' assinged To order with id'" + order.getId());

        } catch (Exception e) {
            System.out.println("Not possible Assignment to an Order...\n" + e);
            e.printStackTrace();
        }

    }


    public OrdersDAO getoD() {
        return oD;
    }

    public void setoD(OrdersDAO oD) {
        this.oD = oD;
    }


    public Orders getOrder() {
        return order;
    }

    public void setOrder(Orders order) {
        this.order = order;
    }

}
