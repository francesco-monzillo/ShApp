package Business;

import Business.Threads.CheckingInterestInOrder;
import Model.Beans.AssignmentDispatcher;
import Model.Beans.Orders;
import Model.Beans.ShippingProperty;
import Model.Beans.User;
import Model.DAOs.OrdersDAO;
import Model.DAOs.ShippingPropertyDAO;
import Model.DAOs.UserDAO;
import Model.Exceptions.NotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

@WebServlet(name = "orderPublishingServlet", value = "/orderPublishingServlet")
public class orderPublishingServlet extends HttpServlet {

    private static OrdersDAO ordersDAO;
    private static ShippingPropertyDAO shippingPropertyDAO;
    private UserDAO userDAO;
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);

        if(session == null) {
            System.out.println("Sessione nulla"); //togliere il test sulla sessione nulla per continuare il debugging domani :)
            response.sendRedirect("homepage.jsp");
            return;
        }

        DataSource ds = (DataSource) getServletContext().getAttribute("DataSource");

        if (ordersDAO == null) {
            ordersDAO = new OrdersDAO(ds);
        }


        if (shippingPropertyDAO == null) {
            shippingPropertyDAO = new ShippingPropertyDAO(ds);
        }

        if (userDAO == null) {
            userDAO = new UserDAO(ds);
        }

        User user = null;

        ObjectMapper oM = new ObjectMapper();

        String[] propertiesPassed = oM.readValue(request.getParameter("properties"), String[].class);


        //Dimensioni
        String lenghtPar = request.getParameter("lenght");
        String widthPar = request.getParameter("width");
        String heightPar = request.getParameter("height");
        String weightPar = request.getParameter("weight");

        //Locazione
        String state = request.getParameter("state");
        String country = request.getParameter("country");
        String district = request.getParameter("district");
        String zipCode = request.getParameter("zipCode");
        String street = request.getParameter("street");
        String streetNumber = request.getParameter("streetNumber");

        //Contatto
        String userEmail = request.getParameter("email");

        AssignmentDispatcher assDisp = (AssignmentDispatcher) session.getAttribute("Account");

        String dispatcherEmail = assDisp.getEmail();

        System.out.println(assDisp.getEmail());

        if (lenghtPar == null) {
            response.setStatus(412);
            response.getWriter().print("Lenght not present");
            return;
        }
        if (widthPar == null) {
            response.setStatus(412);
            response.getWriter().print("Width not present");
            return;
        }
        if (heightPar == null) {
            response.setStatus(412);
            response.getWriter().print("Height not present");
            return;
        }
        if (weightPar == null) {
            response.setStatus(412);
            response.getWriter().print("Weight not present");
            return;
        }


        if (state== null) {
            response.setStatus(412);
            response.getWriter().print("Weight not present");
            return;
        }

        if (country == null) {
            response.setStatus(412);
            response.getWriter().print("Weight not present");
            return;
        }

        if (district == null) {
            response.setStatus(412);
            response.getWriter().print("Weight not present");
            return;
        }

        if (zipCode == null) {
            response.setStatus(412);
            response.getWriter().print("Weight not present");
            return;
        }

        if (street == null) {
            response.setStatus(412);
            response.getWriter().print("Weight not present");
            return;
        }

        if (streetNumber == null) {
            response.setStatus(412);
            response.getWriter().print("Weight not present");
            return;
        }


        if (userEmail != null){
            try{

                user = userDAO.retrieve(userEmail);

            }catch (NotFoundException e){

                user = null;

            }catch (Exception e){

                System.out.println(e);

            }
        }

        Orders o = null;
        try{
            o = new Orders(0, Integer.parseInt(lenghtPar), Integer.parseInt(widthPar), Integer.parseInt(heightPar), Integer.parseInt(weightPar),new Date(Calendar.getInstance().getTime().getTime()), null, null, null, state, country, district, zipCode, state, Integer.parseInt(streetNumber), new AssignmentDispatcher(dispatcherEmail), null, user, null, null);
        }catch (Exception e){
            System.out.println(e);
        }

        ArrayList<ShippingProperty> properties = new ArrayList<>();

        System.out.println("Devo depositare l'ordine nel DB");

        try{

            //Creazione Ordine
            o = ordersDAO.create(o);

            if(propertiesPassed != null && propertiesPassed.length>0) {

                for (int i = 0; i < propertiesPassed.length; i++){
                    properties.add( new ShippingProperty(propertiesPassed[i]) );
                }

            }

            //Associazione eventuali Proprietà
            shippingPropertyDAO.setOrderProperties(o.getId(), properties);

            o.setOrderProperties(properties);

            CheckingInterestInOrder cIO = new CheckingInterestInOrder(o, ordersDAO);
            cIO.start();

            PrintWriter pW = response.getWriter();
            pW.print(""+o.getId());

            response.setStatus(200);

            pW.close();

            return;
            //Preoccuparsi di avviare questo thread per ogni ordine inserito anche nel caso in cui viene riavviata o fermata l'applicazione

        }catch (Exception e){
            //Inserire istruzione per rimuovere dal DB l'ordine inserito
            System.out.println("Ordine non inserito... Di seguito verrà stampato lo stack trace per il debug");
            e.printStackTrace();
            response.setStatus(400);
        }



    }
}
