package Business;

import Model.Beans.Orders;
import Model.Beans.Updates;
import Model.Beans.User;
import Model.DAOs.StateDAO;
import Model.DAOs.UpdatesDAO;
import Model.DAOs.UserDAO;
import Model.Exceptions.NotFoundException;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import javax.sql.DataSource;
import java.io.IOException;
import java.util.ArrayList;

@WebServlet(name = "orderDetailsServlet", value = "/orderDetailsServlet")
public class orderDetailsServlet extends HttpServlet {

    private UpdatesDAO updatesDAO;
    private StateDAO stateDAO;
    private UserDAO userDAO;
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        if(updatesDAO == null){
            updatesDAO = new UpdatesDAO((DataSource) request.getServletContext().getAttribute("DataSource"));
        }

        if(stateDAO == null){
            stateDAO = new StateDAO((DataSource) request.getServletContext().getAttribute("DataSource"));
        }

        if(userDAO == null){
            userDAO = new UserDAO((DataSource) request.getServletContext().getAttribute("DataSource"));
        }


        int orderId = Integer.parseInt(request.getParameter("orderId"));

        ArrayList<Updates> orderUpdates = null;

        try{
            orderUpdates = updatesDAO.retrieveByOrderId(orderId);
        }catch (NotFoundException e){

        }catch (Exception e){
            System.out.println(e);
            request.setAttribute("error","Errore nel tentativo di recupero degli aggiornamenti sull'ordine selezionato... riprovare più tardi");
            RequestDispatcher rD = request.getRequestDispatcher("./homepage.jsp");
            rD.forward(request, response);
            return;
        }

        Updates u = null;
        try{
            u = stateDAO.retrieveLastUpdateByOrder(new Orders(orderId));
            request.setAttribute("orderShipmentState",u.getState().getPhase());
            request.setAttribute("orderShipmentStateDescription",u.getState().getDescription());
        }catch (NotFoundException e){
            request.setAttribute("orderShipmentState","In lavorazione");
            request.setAttribute("orderShipmentStateDescription","");
        }catch (Exception e){
            System.out.println(e);
            request.setAttribute("error","Errore nel tentativo di recupero degllo stato dell'ordine selezionato... riprovare più tardi");
            RequestDispatcher rD = request.getRequestDispatcher("./homepage.jsp");
            rD.forward(request, response);
            return;
        }

        request.setAttribute("userFound",true);
        request.setAttribute("orderId",orderId);
        request.setAttribute("orderUpdates",orderUpdates);
        RequestDispatcher rD = request.getRequestDispatcher("./orderDetail.jsp");
        rD.forward(request, response);
        return;
    }
}
