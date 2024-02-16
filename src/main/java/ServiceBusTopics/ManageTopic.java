package ServiceBusTopics;

import com.azure.core.credential.TokenCredential;
import com.azure.core.management.AzureEnvironment;
import com.azure.core.management.profile.AzureProfile;
import com.azure.identity.*;
import com.azure.messaging.servicebus.administration.ServiceBusAdministrationAsyncClient;
import com.azure.messaging.servicebus.administration.ServiceBusAdministrationClient;
import com.azure.messaging.servicebus.administration.ServiceBusAdministrationClientBuilder;
import com.azure.messaging.servicebus.administration.models.CreateQueueOptions;
import com.azure.messaging.servicebus.administration.models.CreateSubscriptionOptions;
import com.azure.messaging.servicebus.administration.models.CreateTopicOptions;
import com.azure.resourcemanager.webpubsub.models.ManagedIdentity;

import javax.xml.datatype.DatatypeFactory;
import java.time.Duration;

public class ManageTopic {


    public void CreateTopic(String topicName){
        try {

            System.out.println(System.getenv("service_bus_domain"));

            ServiceBusAdministrationClient client = new ServiceBusAdministrationClientBuilder()
                    .credential(System.getenv("service_bus_domain"), new ManagedIdentityCredentialBuilder().build())
                    .buildClient();

            createTopicWithServiceBus(client, topicName);


            System.out.println("Created a Topic with name: "+topicName);

        }catch (Exception e){
            System.out.println(e);
        }

    }

    public static void createTopicWithServiceBus(ServiceBusAdministrationClient client, String topicName){
        CreateTopicOptions options = new CreateTopicOptions();
        try {

            Duration duration = Duration.ofDays(7);
            options.setDefaultMessageTimeToLive(duration);
            client.createTopic(topicName,options);

        }catch (Exception e){
            System.out.println("Error creating duration for Message TTL");
            System.out.println(e);
        }
    }


    public void CreateSubscription(String topicName, String subscriptionName){
        try {

            ServiceBusAdministrationClient client = new ServiceBusAdministrationClientBuilder()
                    .credential(System.getenv("service_bus_domain"), new ManagedIdentityCredentialBuilder().build())
                    .buildClient();
            createSubscriptionWithServiceBus(client, topicName, subscriptionName);

        }catch (Exception e){
            System.out.println(e);
        }

    }

    public static void createSubscriptionWithServiceBus(ServiceBusAdministrationClient client, String topicName, String subscriptionName){
        try {

            client.createSubscription(topicName,subscriptionName);

            System.out.println("Created a Subscription with name: "+subscriptionName+" to Topic: "+topicName);


        }catch (Exception e){
            System.out.println("Error creating duration for Message TTL");
            System.out.println(e);
        }
    }

}
