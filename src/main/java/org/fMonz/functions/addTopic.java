package org.fMonz.functions;

import java.util.*;

import Publish_Subscribe.OrderPublish;
import ServiceBusTopics.ManageTopic;
import com.microsoft.azure.functions.annotation.*;
import com.microsoft.azure.functions.*;

/**
 * Azure Functions with HTTP Trigger.
 */
public class addTopic {
    /**
     * This function listens at endpoint "/api/addTopic". Two ways to invoke it using "curl" command in bash:
     * 1. curl -d "HTTP Body" {your host}/api/addTopic
     * 2. curl {your host}/api/addTopic?name=HTTP%20Query
     */
    @FunctionName("addTopic")
    public HttpResponseMessage run(
            @HttpTrigger(name = "req", methods = {HttpMethod.GET, HttpMethod.POST}, authLevel = AuthorizationLevel.FUNCTION) HttpRequestMessage<Optional<String>> request,
            final ExecutionContext context) {
        context.getLogger().info("Java HTTP trigger processed a request.");

        // Parse query parameter
        String topicName = request.getQueryParameters().get("topicName");

        if (topicName == null) {
            return request.createResponseBuilder(HttpStatus.BAD_REQUEST).body("Please pass a topicName on the query string or in the request body").build();
        } else {

            ManageTopic mT = new ManageTopic();
            mT.CreateTopic(topicName);

            return request.createResponseBuilder(HttpStatus.OK).body("Topic with name '"+topicName+"' created.").build();
        }
    }
}
