package org.fMonz.functions;

import java.util.*;

import ServiceBusTopics.ManageTopic;
import com.microsoft.azure.functions.annotation.*;
import com.microsoft.azure.functions.*;

/**
 * Azure Functions with HTTP Trigger.
 */
public class contractStipulatedWithCourier {

    /**
     * This function listens at endpoint "/api/contractStipulatedWithCourier". Two ways to invoke it using "curl" command in bash:
     * 1. curl -d "HTTP Body" {your host}/api/contractStipulatedWithCourier
     * 2. curl {your host}/api/contractStipulatedWithCourier?name=HTTP%20Query
     */
    @FunctionName("contractStipulatedWithCourier")
    public HttpResponseMessage run(
            @HttpTrigger(name = "req", methods = {HttpMethod.GET, HttpMethod.POST}, authLevel = AuthorizationLevel.FUNCTION) HttpRequestMessage<Optional<String>> request,
            final ExecutionContext context) {
        context.getLogger().info("Java HTTP trigger processed a request.");

        // Parse query parameter
        String subscription = request.getQueryParameters().get("courierName");
        String topic = request.getQueryParameters().get("topicName");
        //String name = request.getBody().orElse(query);

        if (subscription == null || topic == null) {

            if (subscription == null)
                return request.createResponseBuilder(HttpStatus.BAD_REQUEST).body("Please pass a courierName on the query string or in the request body").build();

            return request.createResponseBuilder(HttpStatus.BAD_REQUEST).body("Please pass an topicName on the query string or in the request body").build();

        }

        ManageTopic mT = new ManageTopic();
        mT.CreateSubscription(topic, subscription);
        System.out.println("Subscription Created");




        return request.createResponseBuilder(HttpStatus.OK).body("Hello, " + subscription + "\nYou just subscribed to Topic:" + topic).build();

    }
}
