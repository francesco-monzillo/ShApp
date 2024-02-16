package Publish_Subscribe.Publisher;

import com.azure.messaging.webpubsub.*;
import com.azure.messaging.webpubsub.models.*;

import java.util.ArrayList;

/**
 * Publish messages using Azure Web PubSub service SDK
 *
 */
public class Publisher
{

    static String hub;

    static String message;
    public Publisher(String hub, String connectionString, String message) throws Exception{
        this.hub = hub;
        this.message = message;

        String[] args = new String[1];
        args[0] = connectionString;
        main(args);
    }
    public static void main( String[] args )
    {
        if (args.length != 1) {
            System.out.println("Expecting 3 arguments: <connection-string>");
            return;
        }

        WebPubSubServiceClient service = new WebPubSubServiceClientBuilder()
                .connectionString(args[0])
                .hub(hub)
                .buildClient();


        service.sendToAll(message, WebPubSubContentType.TEXT_PLAIN);
    }
}
