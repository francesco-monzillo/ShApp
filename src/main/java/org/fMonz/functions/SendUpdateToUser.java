package org.fMonz.functions;

import java.util.*;
import java.util.concurrent.CompletableFuture;

import MailService.sendMail;

import com.microsoft.azure.functions.annotation.*;
import com.microsoft.azure.functions.*;

/**
 * Azure Functions with HTTP Trigger.
 */
public class SendUpdateToUser {
    private boolean executionSucceded = true;
    /**
     * This function listens at endpoint "/api/SendUpdateToUser". Two ways to invoke it using "curl" command in bash:
     * 1. curl -d "HTTP Body" {your host}/api/SendUpdateToUser
     * 2. curl {your host}/api/SendUpdateToUser?name=HTTP%20Query
     */
    @FunctionName("SendUpdateToUser")
    public HttpResponseMessage run(
            @HttpTrigger(name = "req", methods = {HttpMethod.GET, HttpMethod.POST}, authLevel = AuthorizationLevel.FUNCTION) HttpRequestMessage<Optional<String>> request,
            final ExecutionContext context) {
        context.getLogger().info("Java HTTP trigger processed a request.");

        // Parse query parameter
        String userMail = request.getQueryParameters().get("receiverAddress");
        String subject = request.getQueryParameters().get("subject");
        String message = request.getQueryParameters().get("message");

        if (userMail == null || subject == null || message == null) {
            if(userMail == null)
                return request.createResponseBuilder(HttpStatus.BAD_REQUEST).body("Please pass the userMail on the query string or in the request body").build();
            if(subject == null)
                return request.createResponseBuilder(HttpStatus.BAD_REQUEST).body("Please pass the subject of the email on the query string or in the request body").build();

            else return request.createResponseBuilder(HttpStatus.BAD_REQUEST).body("Please pass the message of the email on the query string or in the request body").build();

        } else {

            CompletableFuture.runAsync(() -> {
                try {
                    sendMail mail = new sendMail(message, userMail, subject);
                } catch (Exception e) {
                    executionSucceded = false;
                    System.out.println("Some Error Occurred with the Initialization of Subscriber");
                    e.printStackTrace();
                }
            });

            return request.createResponseBuilder(HttpStatus.OK).body("Hello, " + userMail).build();
        }
    }
}
