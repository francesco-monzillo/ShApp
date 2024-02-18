package Business;

import Model.DAOs.UpdatesDAO;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.util.Calendar;

@WebServlet(name = "insertUpdateServlet", value = "/insertUpdateServlet")
public class insertUpdateServlet extends HttpServlet {

    private UpdatesDAO updatesDAO;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {


        if(updatesDAO == null){
            updatesDAO = new UpdatesDAO((DataSource) request.getServletContext().getAttribute("DataSource"));
        }

        String idString = request.getParameter("orderId");
        String statePhase = request.getParameter("statePhase");
        String message = request.getParameter("message");

        if(idString == null){
            System.out.println("Id dell'ordine non passato");

            PrintWriter pW = response.getWriter();
            pW.print("OrderId Not Present");
            pW.close();

            response.setStatus(400);
            return;
        }

        if(statePhase == null){
            System.out.println("Fase della spedizione non passata");

            PrintWriter pW = response.getWriter();
            pW.print("statePhase Not Present");
            pW.close();

            response.setStatus(400);
            return;
        }

        if(message == null){
            System.out.println("Messaggio non passato");

            PrintWriter pW = response.getWriter();
            pW.print("eventual message not passed");
            pW.close();

            response.setStatus(400);
            return;
        }

        int orderId = Integer.parseInt(idString);



        try{
            updatesDAO.createUpdate(orderId, statePhase, message, new Date(Calendar.getInstance().getTime().getTime()));
            response.setStatus(200);
            return;
        }catch (Exception e){
            System.out.println(e);

            PrintWriter pW = response.getWriter();
            pW.print(e);
            pW.close();

            response.setStatus(400);
            return;
        }


    }
}
