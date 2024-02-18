package Business;

import Model.Beans.AssignmentDispatcher;
import Model.Beans.Courier;
import Model.Beans.User;
import Model.DAOs.AssignmentDispatcherDAO;
import Model.DAOs.CourierDAO;
import Model.DAOs.UserDAO;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import javax.sql.DataSource;
import java.io.IOException;


@WebServlet(name = "verifyAccountPresenceServlet", value = "/verifyAccountPresenceServlet")
public class verifyAccountPresenceServlet extends HttpServlet {

    private UserDAO userDAO;
    private AssignmentDispatcherDAO assignmentDispatcherDAO;
    private CourierDAO courierDAO;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        DataSource ds = (DataSource) getServletContext().getAttribute("DataSource");


        if(userDAO == null) {
            userDAO = new UserDAO(ds);
        }

        if(assignmentDispatcherDAO == null){
            assignmentDispatcherDAO = new AssignmentDispatcherDAO(ds);
        }

        if(courierDAO == null){
            courierDAO = new CourierDAO(ds);
        }


        //Valore dell'attributo Access Role: 1:Ruolo End-User; 2:Dispatcher; 3:Corriere


        String email = (String) request.getAttribute("email");

        if(email == null){
            email = request.getParameter("email");
        }

        if(email == null){
            System.out.println("Inserire Email");
            response.setStatus(400);
            return;
        }


        try{
            User endUser = userDAO.retrieve(email);
            HttpSession session = request.getSession(true);
            session.setAttribute("accessRole", 1);
            session.setAttribute("Account", endUser);
            RequestDispatcher rd = request.getRequestDispatcher("./homepage.jsp");
            rd.forward(request, response);
            return;
        }catch (Exception e){
            System.out.println(e);
        }


        try{
            AssignmentDispatcher assDisp = assignmentDispatcherDAO.retrieve(email);
            HttpSession session = request.getSession(true);
            session.setAttribute("accessRole", 2);
            session.setAttribute("Account", assDisp);
            RequestDispatcher rd = request.getRequestDispatcher("./homepage.jsp");
            rd.forward(request, response);
            return;
        }catch (Exception e){
            System.out.println(e);
        }

        try{
            Courier courier = courierDAO.retrieve(email);
            HttpSession session = request.getSession(true);
            session.setAttribute("accessRole", 3);
            session.setAttribute("Account", courier);
            RequestDispatcher rd = request.getRequestDispatcher("./homepage.jsp");
            rd.forward(request, response);
            return;
        }catch (Exception e){
            System.out.println(e);
        }

        RequestDispatcher rd = request.getRequestDispatcher("./signUp.jsp");
        rd.forward(request, response);
        return;

    }
}
