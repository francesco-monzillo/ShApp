package Business;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;


@WebServlet(name = "listenFromTopicServlet", value = "/listenFromTopicServlet")
public class listenFromTopicServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {


        String topic = (String) request.getAttribute("topicName");
        String subscription = (String) request.getAttribute("subscriptionName");


        /*CompletableFuture.runAsync(() -> {
            try {
                ReceiveFromTopic rFT = new ReceiveFromTopic(topic, subscription);
                rFT.receiveMessage(topic, subscription);
            } catch (Exception e) {
                System.out.println("Some Error Occurred with the Initialization of Subscriber");
                e.printStackTrace();

            }
        });*/
    }

}
