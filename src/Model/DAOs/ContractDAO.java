package Model.DAOs;

import Model.Beans.AssignmentDispatcher;
import Model.Beans.Contract;
import Model.Beans.Courier;
import Model.Beans.ShippingProperty;
import Model.Exceptions.NotFoundException;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;

public class ContractDAO {

    private DataSource ds;

    public ContractDAO(DataSource ds) {
        this.ds = ds;
    }


    public Contract retrieve(String assEmail, String couEmail) throws Exception{

        Connection conn = ds.getConnection();

        try{

            PreparedStatement pS = conn.prepareStatement("SELECT c.start, c.end FROM CONTRACT AS c WHERE c.dispatcher_id = ? AND c.courier_id = ?;");
            pS.setString(1,assEmail);
            pS.setString(2,couEmail);

            ResultSet rs = pS.executeQuery();

            if(rs.next()) {
                Date startDate = rs.getDate(1);
                Date endDate = rs.getDate(2);

                AssignmentDispatcherDAO adDao = new AssignmentDispatcherDAO(ds);

                AssignmentDispatcher assDisp = null;

                try{
                    assDisp = adDao.retrieve(assEmail);
                }catch (NotFoundException e){
                    throw new Exception("This contract has not an AssignmentDispatcher anymore");
                }


                CourierDAO courierDAO = new CourierDAO(ds);
                Courier courier = null; //Chiamare la retrieve del CourierDAO sul courierId trovato (solo se tale ordine è gia stato assegnato)

                ShippingPropertyDAO sPDAO = new ShippingPropertyDAO(ds);

                try{
                    courier = courierDAO.retrieve(couEmail);
                }catch (NotFoundException e){
                    System.out.println("Corriere inesistente associato ad un contratto... impossibile");
                }

                Contract contract = new Contract(startDate, endDate, assDisp, courier, null);
                ArrayList<ShippingProperty> properties = null;

                try{
                    properties = sPDAO.agreedProperties(contract);
                    contract.setShippingProperties(properties);
                }catch (NotFoundException e){

                }

                contract.setShippingProperties(properties);
                contract.setCourier(courier);
                return contract;
            }else{
                throw new NotFoundException("No contract agreed between Assignment Dispatcher '" + assEmail + "' and Courier '" + couEmail +"not found...");
            }

        }catch (SQLException e){
            throw new Exception("Problem with retrieving a contract in ContractDAO:\n"+e.getMessage()+"\n"+e.getSQLState());
        }finally{
            conn.close();
        }
    }

    public void create(Date startDate, Date endDate, String dispatcherMail, String courierMail, ArrayList<ShippingProperty> props) throws Exception{

        Connection conn = ds.getConnection();

        try{

            PreparedStatement pS = conn.prepareStatement("INSERT INTO CONTRACT VALUES (?, ?, ?, ?);");
            pS.setDate(1,startDate);
            pS.setDate(2,endDate);
            pS.setString(3,dispatcherMail);
            pS.setString(4,courierMail);

            int result = pS.executeUpdate();

            if(result > 0) {
                for (int i = 0; i < props.size(); i++){
                    addPropsToContract(props.get(i),dispatcherMail,courierMail);
                }

            }else{
                throw new Exception("Can't add A contract between AssignmentDispatcher with e-mail '" + dispatcherMail + "' and Courier with e-mail '" + courierMail +"' not found...");
            }

        }catch (SQLException e){
            throw new Exception("Problem with retrieving a contract in ContractDAO:\n"+e.getMessage()+"\n"+e.getSQLState());
        }finally{
            conn.close();
        }
    }

    public ArrayList<Courier> assDispRetrieveCouriers(String assName) throws Exception{

        Connection conn = ds.getConnection();

        try{

            PreparedStatement pS = conn.prepareStatement("SELECT c.courier_id FROM CONTRACT AS c WHERE c.dispatcher_id = ? ;");
            pS.setString(1,assName);

            ResultSet rs = pS.executeQuery();

            ArrayList<Courier> couriers = new ArrayList<Courier>();

            while(rs.next()) {
                String couName = rs.getString(1);

                CourierDAO cD = new CourierDAO(ds);

                Courier courier = null; //Chiamare la retrieve del CourierDAO sul courierId trovato (solo se tale ordine è gia stato assegnato)

                try {
                    courier = cD.retrieve(couName);
                    couriers.add(courier);
                } catch (NotFoundException e) {

                }
            }

            if(couriers.size()>0) {
                return couriers;
            }else{
                throw new NotFoundException("No Couriers in partnership with AssignmentDispatcher with name '"+assName);
            }

        }catch (SQLException e){
            throw new Exception("Problem with retrieving couriers in partnership with AssignmenrDispatcher AssignmentDispatcherDAO:\n"+e.getMessage()+"\n"+e.getSQLState());
        }finally{
            conn.close();
        }
    }


    public ArrayList<Courier> assDispRetrieveCouriersCurrent(String assName) throws Exception{

        Connection conn = ds.getConnection();

        try{

            PreparedStatement pS = conn.prepareStatement("SELECT c.courier_id FROM CONTRACT AS c WHERE c.dispatcher_id = ? c.end > ?;");
            pS.setString(1,assName);
            pS.setDate(2, new Date(Calendar.getInstance().getTime().getTime()));

            ResultSet rs = pS.executeQuery();

            ArrayList<Courier> couriers = new ArrayList<Courier>();

            while(rs.next()) {
                String couName = rs.getString(1);

                CourierDAO cD = new CourierDAO(ds);

                Courier courier = null; //Chiamare la retrieve del CourierDAO sul courierId trovato (solo se tale ordine è gia stato assegnato)

                try {
                    courier = cD.retrieve(couName);
                    couriers.add(courier);
                } catch (NotFoundException e) {

                }
            }

            if(couriers.size()>0) {
                return couriers;
            }else{
                throw new NotFoundException("No Couriers in partnership with AssignmentDispatcher with name '"+assName);
            }

        }catch (SQLException e){
            throw new Exception("Problem with retrieving couriers in partnership with AssignmenrDispatcher AssignmentDispatcherDAO:\n"+e.getMessage()+"\n"+e.getSQLState());
        }finally{
            conn.close();
        }
    }

    public ArrayList<AssignmentDispatcher> courierRetrieveAssDisp(String courierName) throws Exception{

        Connection conn = ds.getConnection();

        try{

            PreparedStatement pS = conn.prepareStatement("SELECT c.dispatcher_id FROM CONTRACT AS c WHERE c.courier_id = ? ;");
            pS.setString(1,courierName);

            ResultSet rs = pS.executeQuery();

            ArrayList<AssignmentDispatcher> assignmentDispatchers = new ArrayList<AssignmentDispatcher>();

            while(rs.next()) {
                String assName = rs.getString(1);

                AssignmentDispatcherDAO aD = new AssignmentDispatcherDAO(ds);

                AssignmentDispatcher assDisp = null; //Chiamare la retrieve del CourierDAO sul courierId trovato (solo se tale ordine è gia stato assegnato)

                try {
                    assDisp = aD.retrieve(assName);
                    assignmentDispatchers.add(assDisp);
                } catch (NotFoundException e) {

                }
            }

            if(assignmentDispatchers.size()>0) {
                return assignmentDispatchers;
            }else{
                throw new NotFoundException("No AssignmentDispatcher in partnership with Courier with name '"+courierName);
            }

        }catch (SQLException e){
            throw new Exception("Problem with retrieving AssignmentDispatcher in partnership with Courier CourierDAO:\n"+e.getMessage()+"\n"+e.getSQLState());
        }finally{
            conn.close();
        }
    }

    public void addPropsToContract(ShippingProperty sP ,String dispatcherName, String courierName) throws Exception{
        Connection conn = ds.getConnection();

        try{

            PreparedStatement pS = conn.prepareStatement("INSERT INTO CONTRACTOFFER VALUES (?, ?, ?);");
            pS.setString(1, sP.getName());
            pS.setString(2, dispatcherName);
            pS.setString(3, courierName);

            int result = pS.executeUpdate();

            if(result > 0) {
                System.out.println("Property '"+ sP.getName() +"' successfully added to contract between AssignmentDispatcher '" + dispatcherName + "' and Courier '" + courierName + "'");
            }else{
                throw new Exception("Problem with adding property '" + sP.getName() + "' to contract between AssignmentDispatcher '" + dispatcherName + "' and Courier '" + courierName + "'");
            }

        }catch (SQLException e){
            throw new Exception("Problem with creating a Courier in CourierDAO:\n"+e.getMessage()+"\n"+e.getSQLState());
        }finally{
            conn.close();
        }
    }

}
