package Business;

import Model.Beans.AssignmentDispatcher;
import Model.Beans.Courier;
import Model.Beans.Orders;
import Model.Beans.User;
import Model.DAOs.OrdersDAO;
import Model.DAOs.UserDAO;
import Model.Exceptions.NotFoundException;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import javax.sql.DataSource;
import javax.swing.text.html.HTML;
import java.io.IOException;
import java.util.ArrayList;

@WebServlet(name = "visualizeOrdersServlet", value = "/visualizeOrdersServlet")
public class visualizeOrdersServlet extends HttpServlet {

    private OrdersDAO ordersDAO;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        DataSource ds = (DataSource) getServletContext().getAttribute("DataSource");

        if(ordersDAO == null) {
            ordersDAO = new OrdersDAO(ds);
        }

        ArrayList<Orders> orders = null;

        HttpSession session = request.getSession(false);

        if(session == null) {
            System.out.println("Nessun Accesso rimasto in sessione... ritorno alla homepage");
            response.sendRedirect("homepage.jsp");
            return;
        }

        int accessRole = (int) session.getAttribute("accessRole");


        try {

            if (accessRole == 1) {
                User user = (User) session.getAttribute("Account");
                int activeShipment = Integer.parseInt(request.getParameter("active"));

                orders = ordersDAO.retrieveByEndUser(user.getEmail());

                System.out.println(orders);
                if(activeShipment == 1)
                    request.setAttribute("onlyNotAlreadyDelivered",true);



            } else if (accessRole == 2) {

                AssignmentDispatcher dispatcher = (AssignmentDispatcher) session.getAttribute("Account");
                orders = ordersDAO.retrieveByDispatcher(dispatcher.getEmail());

            } else {

                Courier courier = (Courier) session.getAttribute("Account");
                orders = ordersDAO.retrieveByCourier(courier.getEmail());
                int activeShipment = Integer.parseInt(request.getParameter("active"));
                if(activeShipment == 1)
                    request.setAttribute("onlyNotAlreadyDelivered",true);

            }


            request.setAttribute("orderList", orders);
            RequestDispatcher rD = request.getRequestDispatcher("./ordersAssociated.jsp");
            rD.forward(request, response);
            return;

        }catch (NotFoundException e){
            request.setAttribute("orderList",null);
            RequestDispatcher rD = request.getRequestDispatcher("./ordersAssociated.jsp");
            rD.forward(request, response);
            return;
        }catch (Exception e){
            System.out.println("Errore nel recupero degli ordini associati all'account loggato... ritorno status code di errore 400");
            System.out.println(e);
            response.setStatus(400);
            return;
        }

    }
}
