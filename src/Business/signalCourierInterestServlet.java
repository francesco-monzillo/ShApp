package Business;

import Model.Beans.Courier;
import Model.DAOs.CourierDAO;
import Model.DAOs.OrdersDAO;
import Model.Exceptions.AlreadyFoundUnivocalIdentifierException;

import javax.print.attribute.standard.OrientationRequested;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Date;
import java.util.Calendar;

@WebServlet(name = "signalCourierInterestServlet", value = "/signalCourierInterestServlet")
public class signalCourierInterestServlet extends HttpServlet {

    private OrdersDAO ordersDAO;
    private CourierDAO courierDAO;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        if(ordersDAO == null){
            ordersDAO = new OrdersDAO((DataSource) request.getServletContext().getAttribute("DataSource"));
        }

        if(courierDAO == null){
            courierDAO = new CourierDAO((DataSource) request.getServletContext().getAttribute("DataSource"));
        }

        String courierName = request.getParameter("courierName");
        String trackingCode = request.getParameter("trackingCode");
        Date expectedDeliveryDate = null;
        try{
            expectedDeliveryDate = new Date(Date.valueOf(request.getParameter("expectedDeliveryDate")).getTime());
        }catch (Exception e){
            System.out.println("Problema con la traduzione della data nella signalCourierInterestServlet");
        }
        String orderIdString = request.getParameter("orderId");

        if(courierName == null){
            response.setStatus(400);
            return;
        }

        if(trackingCode == null){
            response.setStatus(400);
            return;
        }

        if(expectedDeliveryDate == null){
            response.setStatus(400);
            return;
        }

        if(orderIdString == null){
            response.setStatus(400);
            return;
        }

        int orderId = Integer.parseInt(orderIdString);

        try{
            Courier c = courierDAO.retrieveByName(courierName);

            ordersDAO.insertInterestedCourier(c.getEmail(), orderId, new Date(Calendar.getInstance().getTime().getTime()), expectedDeliveryDate, trackingCode);
            response.setStatus(200);

            return;
        }catch(AlreadyFoundUnivocalIdentifierException aE){
            System.out.println("Already reported interest by courier with name '" + courierName + "'");
            response.setStatus(200);
            return;
        }catch(Exception e){
            System.out.println(e);
            response.setStatus(400);
        }

    }
}
