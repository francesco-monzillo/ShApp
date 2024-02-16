package Publish_Subscribe;

import com.azure.core.credential.TokenCredential;
import com.azure.core.management.AzureEnvironment;
import com.azure.core.management.profile.AzureProfile;
import com.azure.identity.DefaultAzureCredentialBuilder;
import com.azure.resourcemanager.webpubsub.WebPubSubManager;
import com.azure.resourcemanager.webpubsub.models.EventHandler;
import com.azure.resourcemanager.webpubsub.models.EventHubEndpoint;
import com.azure.resourcemanager.webpubsub.models.EventListener;
import com.azure.resourcemanager.webpubsub.models.EventNameFilter;
import com.azure.resourcemanager.webpubsub.models.ManagedIdentitySettings;
import com.azure.resourcemanager.webpubsub.models.UpstreamAuthSettings;
import com.azure.resourcemanager.webpubsub.models.UpstreamAuthType;
import com.azure.resourcemanager.webpubsub.models.WebPubSubHubProperties;
import java.util.Arrays;

/** Samples for WebPubSubHubs CreateOrUpdate. */
public final class CreateHub {
    /*
     * x-ms-original-file: specification/webpubsub/resource-manager/Microsoft.SignalRService/stable/2023-02-01/examples/WebPubSubHubs_CreateOrUpdate.json
     */
    /**
     * Sample code: WebPubSubHubs_CreateOrUpdate.
     *
     * @param manager Entry point to WebPubSubManager.
     */
    private static String hubName;

    public CreateHub(String hubName){
        this.hubName = hubName;
        AzureProfile profile = new AzureProfile("c30767db-3dda-4dd4-8a4d-097d22cb99d3","9857edd1-8563-4298-a704-749e75027dee", AzureEnvironment.AZURE);
        TokenCredential credential = new DefaultAzureCredentialBuilder()
                .authorityHost(profile.getEnvironment().getActiveDirectoryEndpoint())
                .build();
        WebPubSubManager manager = WebPubSubManager
                .authenticate(credential, profile);

        webPubSubHubsCreateOrUpdate(manager);
    }
    public static void webPubSubHubsCreateOrUpdate(com.azure.resourcemanager.webpubsub.WebPubSubManager manager) {
        manager
                .webPubSubHubs()
                .define(hubName)
                .withExistingWebPubSub("Business", "assignmentPubSub")
                .withProperties(
                        new WebPubSubHubProperties()
                                .withEventHandlers(
                                        Arrays
                                                .asList(
                                                        new EventHandler()
                                                                .withUrlTemplate("http://host.com")
                                                                .withUserEventPattern("*")
                                                                .withSystemEvents(Arrays.asList("connect", "connected"))
                                                                .withAuth(
                                                                        new UpstreamAuthSettings()
                                                                                .withType(UpstreamAuthType.MANAGED_IDENTITY)
                                                                                    .withManagedIdentity(new ManagedIdentitySettings().withResource("ShAppPubSub")))))
                                .withEventListeners(
                                        Arrays
                                                .asList(
                                                        new EventListener()
                                                                .withFilter(
                                                                        new EventNameFilter()
                                                                                .withSystemEvents(Arrays.asList("connected", "disconnected"))
                                                                                .withUserEventPattern("*"))
                                                                .withEndpoint(
                                                                        new EventHubEndpoint()
                                                                                .withFullyQualifiedNamespace(hubName+ ".servicebus.windows.net")
                                                                                .withEventHubName("event"+hubName))))
                                .withAnonymousConnectPolicy("allow"))
                .create();
        }
    }
