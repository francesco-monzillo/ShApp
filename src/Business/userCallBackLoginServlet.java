package Business;

import com.azure.core.implementation.jackson.ObjectMapperShim;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.LinkedHashMap;

@WebServlet(name = "userCallBackLoginServlet", value = "/userCallBackLoginServlet")
public class userCallBackLoginServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {


        System.out.println("Ciao");

        if(request.getParameter("passedData") == null){
            System.out.println("dati mancanti");
            response.setStatus(400);
            return;
        }

        ObjectMapper objM = new ObjectMapper();

        ObjectMapperShim obj = new ObjectMapperShim(objM);

        Hashtable<String,Object> passedData[] = new Hashtable[7];


        try{
            passedData = obj.readValue(request.getParameter("passedData"),Hashtable[].class);
        }catch (Exception e){
            System.out.println("Errore nella traduzione da stringa a oggetto");

            System.out.println(e);
        }

        String userEmailId = null;
        String userName = null;
        String userSurname = null;

        try {
            for (Hashtable<String, Object> element : passedData) {

                System.out.println(element);

                Object userId = element.get("user_id");

                if (userId != null)
                    userEmailId = (String) userId;

                Object userClaims = element.get("user_claims");

                if (userClaims != null) {
                    ArrayList<LinkedHashMap> user_claims = (ArrayList<LinkedHashMap>) userClaims;
                    userName = (String) user_claims.get(9).get("val");
                    userSurname = (String) user_claims.get(10).get("val");
                }

            }


            System.out.println("Data Extracted:\nEmail: " + userEmailId + "\nName: " + userName + "\nSurname: " + userSurname);

            request.setAttribute("email",userEmailId);

            RequestDispatcher rD = request.getRequestDispatcher("./verifyAccountPresenceServlet");
            rD.forward(request, response);

            //Eseguire il forward della richiesta all' userLoginServlet per stabilire se tale utente esiste o meno nel db ed eventualmente inserirlo nella sessione con il suo ruolo e dati appropriati,
            //Altrimenti crearlo con il ruolo di utente se non ha dichiarato di essere un corriere o e-commerce;
            //Se ha dichiarato di essere un corriere o e-commerce inserire una sorta di richiesta di sign-up nel database che dovrà essere approvata in separata sede(In maniera asincrona)

        }catch (Exception e){
            System.out.println(e);
        }

    }
}
