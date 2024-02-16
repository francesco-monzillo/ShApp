package Publish_Subscribe.Couriers;
import Publish_Subscribe.OrderPublish;
import com.azure.messaging.webpubsub.*;
import com.azure.messaging.webpubsub.models.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;


import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

/**
 * Connect to Azure Web PubSub service using WebSocket protocol
 */
public class Subscriber

{

    private String hub;
    private String courierName;

    private Class<OrderPublish> messageClass = OrderPublish.class;

    public Subscriber(String hub, String connString, String courierName) throws Exception{
        this.hub = hub;
        this.courierName = courierName;
        String [] args = new String[1];
        args[0]= connString;
        main(args);
    }

    public void main( String[] args ) throws IOException, URISyntaxException {
        if (args.length != 1) {
            System.out.println("Expecting 1 argument: <connection-string>");
            return;
        }


        WebPubSubServiceClient service = new WebPubSubServiceClientBuilder()
                .connectionString(args[0])
                .hub(hub)
                .buildClient();


            WebPubSubClientAccessToken token = service.getClientAccessToken(new GetClientAccessTokenOptions());

            String currHub = hub;

            System.out.println("Courier:"+ courierName +"\nSubscribed to Hub:"+hub);

            System.out.println(token.getUrl());
            WebSocketClient webSocketClient = new WebSocketClient(new URI(token.getUrl())) {
                String hub = currHub;
                @Override
                public void onMessage(String message) {
                    System.out.println(String.format("Hub: '"+ currHub +"'\n"+"Subscriber Name: '" + courierName+"'\nMessage received: %s", message));

                    ObjectMapper mapper = new ObjectMapper();
                    try {
                        OrderPublish or = mapper.readValue(message, messageClass);
                        System.out.println("Order received: "+"\n"+or.toString());
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }


                    synchronized(this) {
                        this.notify();
                    }

                }

                @Override
                public void onClose(int arg0, String arg1, boolean arg2) {
                    System.out.println("Courier: "+courierName+"\nSubscription to hub: "+ hub +" closed");
                    System.out.println(arg0+"\n"+arg1+"\n"+arg2);
                    Thread.currentThread().interrupt();
                }

                @Override
                public void onError(Exception arg0) {
                    System.out.println("Problem on message receiving");
                    arg0.printStackTrace();
                    Thread.currentThread().interrupt();
                }

                @Override
                public void onOpen(ServerHandshake arg0) {
                    System.out.println(this.isOpen());

                }

            };

            try{
                webSocketClient.connectBlocking();
            }catch (InterruptedException e){
                System.out.println("Connection interrupted");
                e.printStackTrace();
            }

            synchronized (webSocketClient){
                while (true) {
                    try {
                        webSocketClient.wait();
                    } catch (InterruptedException e) {
                        break;
                    }
                }
            }


        }
    }
