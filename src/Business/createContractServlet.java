package Business;

import Model.Beans.AssignmentDispatcher;
import Model.Beans.Courier;
import Model.Beans.ShippingProperty;
import Model.DAOs.AssignmentDispatcherDAO;
import Model.DAOs.ContractDAO;
import Model.DAOs.CourierDAO;
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

@WebServlet(name = "createContractServlet", value = "/createContractServlet")
public class createContractServlet extends HttpServlet {

    private ContractDAO contractDAO;
    private CourierDAO courierDAO;
    private AssignmentDispatcherDAO assignmentDispatcherDAO;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        if(contractDAO == null){
            contractDAO = new ContractDAO((DataSource) request.getServletContext().getAttribute("DataSource"));
        }

        if(courierDAO == null){
            courierDAO = new CourierDAO((DataSource) request.getServletContext().getAttribute("DataSource"));
        }

        if(assignmentDispatcherDAO == null){
            assignmentDispatcherDAO = new AssignmentDispatcherDAO((DataSource) request.getServletContext().getAttribute("DataSource"));
        }

        String dispatcherName = request.getParameter("dispatcher");
        String courierName = request.getParameter("courier");

        String dataInizio = request.getParameter("dataInizio");

        if(dataInizio==null){
            response.getWriter().print("initialDate");
            response.setStatus(400);
            return;
        }
        Date realInitialDate = Date.valueOf(dataInizio);


        String dataFine = request.getParameter("dataFine");

        if(dataFine==null){
            response.getWriter().print("initialDate");
            response.setStatus(400);
            return;
        }
        Date realFinalDate = Date.valueOf(dataFine);

        ObjectMapper oM = new ObjectMapper();

        String[] propertiesPassed = oM.readValue(request.getParameter("properties"), String[].class);

        ArrayList<ShippingProperty> propsOfContract = new ArrayList<>();

        if(propertiesPassed != null) {

            for (int i = 0; i < propertiesPassed.length; i++) {
                propsOfContract.add(new ShippingProperty(propertiesPassed[i]));
            }

        }

        Courier c = null;
        try{
            c = courierDAO.retrieveByName(courierName);
        }catch (NotFoundException e){
            PrintWriter pW = response.getWriter();
            pW.print("Courier Not found");

            response.setStatus(400);

            pW.close();
            return;
        }catch (Exception e){
            System.out.println(e);
            response.setStatus(400);
            return;
        }

        AssignmentDispatcher assDisp = null;

        try{
            assDisp = assignmentDispatcherDAO.retrieve(dispatcherName);
        }catch (Exception e){
            System.out.println(e);
            response.setStatus(400);
            return;
        }

        try{
            contractDAO.create(realInitialDate, realFinalDate, dispatcherName, c.getEmail(), propsOfContract);

            /*request.setAttribute("topicName", assDisp.getName());
            request.setAttribute("subscriptionName", courierName);
            RequestDispatcher rd = request.getRequestDispatcher("./listenFromTopicServlet");

            rd.include(request, response);*/

        }catch (Exception e){
            System.out.println(e);
            PrintWriter pW = response.getWriter();
            pW.print("Contract");

            response.setStatus(400);

            pW.close();
        }


    }
}
