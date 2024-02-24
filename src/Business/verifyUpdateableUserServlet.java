package Business;

import Model.Beans.User;
import Model.DAOs.UserDAO;
import Model.Exceptions.NotFoundException;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "verifyUpdateableUserServlet", value = "/verifyUpdateableUserServlet")
public class verifyUpdateableUserServlet extends HttpServlet {

    private UserDAO userDAO;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        if(userDAO == null){
            userDAO = new UserDAO((DataSource) request.getServletContext().getAttribute("DataSource"));
        }

        String email = request.getParameter("email");

        if(email == null){
            response.setStatus(400);
            return;
        }

        User u = null;

        try{
            u = userDAO.retrieve(email);


            PrintWriter pW = response.getWriter();
            pW.print(""+u.getName());

            response.setStatus(200);

            pW.close();

            return;
        }catch (NotFoundException e){

            PrintWriter pW = response.getWriter();
            pW.print(e);
            pW.close();

            response.setStatus(400);
            return;
        }catch (Exception e){
            System.out.println(e);
            response.setStatus(405);
            return;
        }
    }
}
