package MailService;

import com.azure.communication.email.models.*;
import com.azure.communication.email.*;
import com.azure.core.util.polling.LongRunningOperationStatus;
import com.azure.core.util.polling.PollResponse;
import com.azure.core.util.polling.SyncPoller;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;


public class sendMail {

    private String message;
    private String receiverAddress;

    private String subject;


    public sendMail(String message, String receiverAddress, String subject) throws Exception{
        this.message = message;
        this.receiverAddress = receiverAddress;
        this.subject = subject;

        String[] args = new String[1];
        main(args);

    }

    public void main(String[] args) {
        String emailConnectionString = "endpoint=https://shappemailservice.unitedstates.communication.azure.com/;accesskey=E+iF6F+4Ge9WWkQqIKd1GlQ6AZ4QdkMgwECsL+fZ+dyGB8BSRsqdVpXanC0L0qvPzPBxkdVE0WocEcjGioC6jw==";
        EmailClient emailClient = new EmailClientBuilder()
                .connectionString(emailConnectionString)
                .buildClient();

        EmailMessage emailMessage = new EmailMessage()
                .setSenderAddress("DoNotReply@150a5098-2527-40d0-9dea-00ae865adf63.azurecomm.net")
                .setToRecipients(receiverAddress)
                .setSubject(subject)
                .setBodyPlainText(message);

        try {
            SyncPoller<EmailSendResult, EmailSendResult> poller = emailClient.beginSend(emailMessage, null);

            PollResponse<EmailSendResult> pollResponse = null;

            Duration timeElapsed = Duration.ofSeconds(0);
            Duration POLLER_WAIT_TIME = Duration.ofSeconds(10);

            while (pollResponse == null
                    || pollResponse.getStatus() == LongRunningOperationStatus.NOT_STARTED
                    || pollResponse.getStatus() == LongRunningOperationStatus.IN_PROGRESS) {
                pollResponse = poller.poll();
                System.out.println("Email send poller status: " + pollResponse.getStatus());

                Thread.sleep(POLLER_WAIT_TIME.toMillis());
                timeElapsed = timeElapsed.plus(POLLER_WAIT_TIME);

                if (timeElapsed.compareTo(POLLER_WAIT_TIME.multipliedBy(18)) >= 0) {
                    throw new RuntimeException("Polling timed out.");
                }
            }

            if (poller.getFinalResult().getStatus() == EmailSendStatus.SUCCEEDED) {
                System.out.printf("Successfully sent the email (operation id: %s)", poller.getFinalResult().getId());
            } else {
                throw new RuntimeException(poller.getFinalResult().getError().getMessage());
            }

        }catch (InterruptedException e){
            System.out.println("Email Function Thread Interrupted... Investigating further...");
        }
    }

}
