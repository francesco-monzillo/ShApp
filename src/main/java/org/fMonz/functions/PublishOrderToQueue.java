package org.fMonz.functions;

import java.util.*;
import java.util.concurrent.CompletableFuture;

import ServiceBusTopics.PublishToTopic;
import com.microsoft.azure.functions.annotation.*;
import com.microsoft.azure.functions.*;

/**
 * Azure Functions with HTTP Trigger.
 */
public class PublishOrderToQueue {

    private boolean executionSucceded = true;
    /**
     * This function listens at endpoint "/api/PublishOrderToQueue". Two ways to invoke it using "curl" command in bash:
     * 1. curl -d "HTTP Body" {your host}/api/PublishOrderToQueue
     * 2. curl {your host}/api/PublishOrderToQueue?name=HTTP%20Query
     */
    @FunctionName("PublishOrderToQueue")
    public HttpResponseMessage run(
            @HttpTrigger(name = "req", methods = {HttpMethod.GET, HttpMethod.POST}, authLevel = AuthorizationLevel.FUNCTION) HttpRequestMessage<Optional<String>> request,
            final ExecutionContext context) {
        context.getLogger().info("Java HTTP trigger processed a request.");

        // Parse query parameter
        String message = request.getQueryParameters().get("message");
        String topic = request.getQueryParameters().get("topicName");

        System.out.println("Message:" + message + "\nTopic: " + topic);
        //String name = request.getBody().orElse(query);


        if (message == null || topic == null) {

            if (message == null)
                return request.createResponseBuilder(HttpStatus.BAD_REQUEST).body("Please pass a message on the query string or in the request body").build();

            return request.createResponseBuilder(HttpStatus.BAD_REQUEST).body("Please pass an topicName on the query string or in the request body").build();

        }

        try{
            PublishToTopic pTT = new PublishToTopic();
            pTT.publish(topic, message);
        }catch (Exception e){
            executionSucceded = false;
            System.out.println("Some Error Occurred with the Initialization of Publisher");
            e.printStackTrace();
        }


        if(executionSucceded)
            return request.createResponseBuilder(HttpStatus.OK).build();
        else
            return request.createResponseBuilder(HttpStatus.BAD_REQUEST).body("Invalid connString").build();

    }
}
