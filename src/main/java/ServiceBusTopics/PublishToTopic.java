package ServiceBusTopics;

import com.azure.identity.ManagedIdentityCredentialBuilder;
import com.azure.messaging.servicebus.ServiceBusClientBuilder;
import com.azure.messaging.servicebus.ServiceBusMessage;
import com.azure.messaging.servicebus.ServiceBusSenderClient;

public class PublishToTopic {



    public PublishToTopic(){

    }


    public void publish(String topicToPublish, String message){
        String topicName = topicToPublish;

        ServiceBusSenderClient senderClient = new ServiceBusClientBuilder()
                .credential(System.getenv("service_bus_domain"),new ManagedIdentityCredentialBuilder().build())
                .sender()
                .topicName(topicName)
                .buildClient();

        try {
            // send one message to the topic
            senderClient.sendMessage(new ServiceBusMessage(message));
            System.out.println("Sent a single message to the topic: " + topicName);

        }catch (Exception e){
            System.out.println("Errore nell'invio del messaggio sul topic: " +topicName+" ...");
            System.out.println(e);
        } finally {
            senderClient.close();
        }

    }

}
