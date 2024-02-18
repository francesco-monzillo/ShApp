package Business;

import Model.Beans.AssignmentDispatcher;
import Model.Beans.Courier;
import Model.Beans.User;
import Model.DAOs.AssignmentDispatcherDAO;
import Model.DAOs.CourierDAO;
import Model.DAOs.UserDAO;
import Model.Exceptions.NotFoundException;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import javax.sql.DataSource;
import java.io.IOException;

@WebServlet(name = "signUpNewUser", value = "/signUpNewUser")
public class signUpNewUser extends HttpServlet {

    private UserDAO userDAO;
    private AssignmentDispatcherDAO assignmentDispatcherDAO;
    private CourierDAO courierDAO;


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        DataSource ds = (DataSource) request.getServletContext().getAttribute("DataSource");

        if(userDAO == null){
            userDAO = new UserDAO(ds);
        }

        if(assignmentDispatcherDAO == null){
            assignmentDispatcherDAO = new AssignmentDispatcherDAO(ds);
        }

        if(courierDAO == null){
            courierDAO = new CourierDAO(ds);
        }

        String email = request.getParameter("email").trim();
        String name = request.getParameter("name").trim();
        String phoneNumber = request.getParameter("phoneNumber").trim();

        int accountType = Integer.parseInt(request.getParameter("accountType"));

        System.out.println("Email :" + email + "\nName: " + name + "\nPhoneNumber: " + phoneNumber + "\nAccountType: " + accountType);

        if(accountType == 1){
            try{
                userDAO.retrieve(email);
                response.setStatus(400);
                return;
            }catch (NotFoundException nf){
                String surname = request.getParameter("surname");
                try{
                    User u = userDAO.create(email, name, surname, phoneNumber);
                    HttpSession session = request.getSession(true);
                    session.setAttribute("Account", u);
                    session.setAttribute("accessRole",1);

                    response.setStatus(200);
                    return;

                }catch (Exception exc){
                    System.out.println(exc);
                    exc.printStackTrace();
                    response.setStatus(404);
                    return;
                }
            }catch (Exception e){
                System.out.println(e);
                e.printStackTrace();
                response.setStatus(404);
                return;
            }
        }else if(accountType == 2){
            try{
                try{
                    assignmentDispatcherDAO.retrieve(email);
                }catch (NotFoundException e){
                    assignmentDispatcherDAO.retrieveByName(name);
                    response.setStatus(401);
                    return;
                }
                response.setStatus(400);
                return;
            }catch (NotFoundException nf){
                try {
                    AssignmentDispatcher assDisp = assignmentDispatcherDAO.create(name, email);
                    HttpSession session = request.getSession(true);
                    session.setAttribute("Account", assDisp);
                    session.setAttribute("accessRole", 2);

                    response.setStatus(200);
                    return;
                }catch (Exception exc){
                    System.out.println(exc);
                    exc.printStackTrace();
                    response.setStatus(404);
                    return;
                }
            }catch (Exception exc){
                System.out.println(exc);
                exc.printStackTrace();
                response.setStatus(404);
                return;
            }
        }else if(accountType == 3){
            try{
                try{
                    courierDAO.retrieve(email);
                }catch (NotFoundException e){
                    courierDAO.retrieveByName(name);
                    response.setStatus(401);
                    return;
                }
                response.setStatus(400);
                return;
            }catch (NotFoundException nf){
                try {
                    Courier courier = courierDAO.create(name, email);
                    HttpSession session = request.getSession(true);
                    session.setAttribute("Account", courier);
                    session.setAttribute("accessRole", 3);

                    response.setStatus(200);
                    return;
                }catch (Exception exc){
                    System.out.println(exc);
                    exc.printStackTrace();
                    response.setStatus(404);
                    return;
                }
            }catch (Exception exc){
                System.out.println(exc);
                exc.printStackTrace();
                response.setStatus(404);
                return;
            }
        }else{
            System.out.println("Wrong accountType passed... value: " + accountType);
            response.setStatus(405);
            return;
        }

    }
}
