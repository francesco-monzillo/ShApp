package Business.receiverClient;

import Business.OrderPublish;
import com.azure.identity.ManagedIdentityCredentialBuilder;
import com.azure.messaging.servicebus.*;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.DataOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.Date;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class ReceiveFromTopic {

    private String topicName;
    private String subscriberName;

    public ReceiveFromTopic(String topicName, String subscriberName){
        this.topicName = topicName;
        this.subscriberName = subscriberName;
    }


    public void receiveMessage(String topicName, String subscriberName){

        CountDownLatch countdownLatch = new CountDownLatch(1);

        // Create an instance of the processor through the ServiceBusClientBuilder
        ServiceBusProcessorClient processorClient = new ServiceBusClientBuilder()
                .connectionString(System.getenv("service_bus_endpoint"))
                .processor()
                .topicName(topicName)
                .subscriptionName(subscriberName)
                .processMessage(this::processMessage)
                .processError(context -> processError(context, countdownLatch))
                .buildProcessorClient();

        System.out.println("Starting the processor");
        processorClient.start();

        try {
            while(true) {
                TimeUnit.SECONDS.sleep(120);
            }
        }catch (InterruptedException e) {
            System.out.println("Unexpected suspension of thread to receive messages...");
            System.out.println(e);
            System.out.println("Stopping and closing the processor");
            processorClient.close();
        }
    }

    private void processMessage(ServiceBusReceivedMessageContext context) {

        ServiceBusReceivedMessage message = context.getMessage();
        System.out.printf("Processing message. Session: %s, Sequence #: %s. Contents: %s%n", message.getMessageId(),
                message.getSequenceNumber(), message.getBody());

        ObjectMapper mapper = new ObjectMapper();
        try {
            OrderPublish or = mapper.readValue(message.getBody().toString(), OrderPublish.class);
            System.out.println("Order received: "+"\n"+or.toString());

            if(signalInterest(or)){
                try{
                    URL url = new URL("https://shappweb.azurewebsites.net/signalCourierInterestServlet");
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    con.setRequestMethod("POST");
                    con.setConnectTimeout(5000);
                    con.setReadTimeout(5000);

                    Map<String, String> parameters = new HashMap<>();

                    parameters.put("courierName", subscriberName);

                    Date expectedDeliveryDate = new Date(Calendar.getInstance().getTime().getTime());
                    expectedDeliveryDate.setTime(System.currentTimeMillis() + 172800000); //172800000 Millisecondi in 2 giorni

                    parameters.put("expectedDeliveryDate", expectedDeliveryDate.toString());

                    parameters.put("trackingCode", getAlphaNumericString(12));

                    parameters.put("orderId",String.valueOf(or.getId()));

                    con.setDoOutput(true);
                    DataOutputStream out = new DataOutputStream(con.getOutputStream());
                    out.writeBytes(getParamsString(parameters));
                    out.flush();
                    out.close();

                    con.connect();

                    int statusCode = con.getResponseCode();
                    System.out.println(statusCode);

                    System.out.println(getParamsString(parameters));

                    if(statusCode == 200){
                        System.out.println("Courier with Subscription: '"+ subscriberName+ "' to Publisher '" + topicName + "' successfully reported his interest");
                    }else{
                        System.out.println("Something wrong with the formatting of the request");
                    }
                }catch (Exception e){
                    System.out.println(e);
                }

                // Request parameters and other properties.

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static void processError(ServiceBusErrorContext context, CountDownLatch countdownLatch) {
        System.out.printf("Error when receiving messages from namespace: '%s'. Entity: '%s'%n",
                context.getFullyQualifiedNamespace(), context.getEntityPath());

        if (!(context.getException() instanceof ServiceBusException)) {
            System.out.printf("Non-ServiceBusException occurred: %s%n", context.getException());
            return;
        }

        ServiceBusException exception = (ServiceBusException) context.getException();
        ServiceBusFailureReason reason = exception.getReason();

        if (reason == ServiceBusFailureReason.MESSAGING_ENTITY_DISABLED
                || reason == ServiceBusFailureReason.MESSAGING_ENTITY_NOT_FOUND
                || reason == ServiceBusFailureReason.UNAUTHORIZED) {
            System.out.printf("An unrecoverable error occurred. Stopping processing with reason %s: %s%n",
                    reason, exception.getMessage());

            countdownLatch.countDown();
        } else if (reason == ServiceBusFailureReason.MESSAGE_LOCK_LOST) {
            System.out.printf("Message lock lost for message: %s%n", context.getException());
        } else if (reason == ServiceBusFailureReason.SERVICE_BUSY) {
            try {
                // Choosing an arbitrary amount of time to wait until trying again.
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                System.err.println("Unable to sleep for period of time");
            }
        } else {
            System.out.printf("Error source %s, reason %s, message: %s%n", context.getErrorSource(),
                    reason, context.getException());
        }
    }



    public boolean signalInterest(OrderPublish orderPublish){
        //Logic of the courier to signal interest
        Random r = new Random();
        int number = r.nextInt(5) + 1;

        if(number > 1) {
            System.out.println("Order Accepted");
            return true;
        }
        else {
            System.out.println("Order NOT Accepted");
            return false;
        }
    }

    public static String getParamsString(Map<String, String> params)
            throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();

        for (Map.Entry<String, String> entry : params.entrySet()) {
            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
            result.append("&");
        }

        String resultString = result.toString();
        return resultString.length() > 0
                ? resultString.substring(0, resultString.length() - 1)
                : resultString;
    }

    public static String getAlphaNumericString(int n)
    {

        // choose a Character random from this String
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                + "0123456789"
                + "abcdefghijklmnopqrstuvxyz";

        // create StringBuffer size of AlphaNumericString
        StringBuilder sb = new StringBuilder(n);

        for (int i = 0; i <= n; i++) {

            // generate a random number between
            // 0 to AlphaNumericString variable length
            int index
                    = (int)(AlphaNumericString.length()
                    * Math.random());

            // add Character one by one in end of sb
            sb.append(AlphaNumericString
                    .charAt(index));
        }

        return sb.toString();
    }
}
