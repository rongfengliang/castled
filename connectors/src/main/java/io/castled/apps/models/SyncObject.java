package io.castled.apps.models;

<<<<<<< HEAD
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.castled.apps.ExternalAppType;
import io.castled.apps.connectors.googlepubsub.GooglePubSubTopicSyncObject;
import io.castled.apps.connectors.mailchimp.MailchimpAudienceSyncObject;
import io.castled.apps.connectors.restapi.RestApiAppSyncConfig;
import lombok.*;

@Data
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        visible = true,
        property = "appType")
@JsonSubTypes({
        @JsonSubTypes.Type(value = GenericSyncObject.class, name = "SALESFORCE"),
        @JsonSubTypes.Type(value = GenericSyncObject.class, name = "HUBSPOT"),
        @JsonSubTypes.Type(value = GenericSyncObject.class, name = "GOOGLEADS"),
        @JsonSubTypes.Type(value = MailchimpAudienceSyncObject.class, name = "MAILCHIMP"),
        @JsonSubTypes.Type(value = GenericSyncObject.class, name = "INTERCOM"),
        @JsonSubTypes.Type(value = GenericSyncObject.class, name = "SENDGRID"),
        @JsonSubTypes.Type(value = GenericSyncObject.class, name = "MARKETO"),
        @JsonSubTypes.Type(value = GenericSyncObject.class, name = "KAFKA"),
        @JsonSubTypes.Type(value = GenericSyncObject.class, name = "ACTIVECAMPAIGN"),
        @JsonSubTypes.Type(value = GenericSyncObject.class, name = "CUSTOMERIO"),
        @JsonSubTypes.Type(value = GooglePubSubTopicSyncObject.class, name = "GOOGLEPUBSUB"),
        @JsonSubTypes.Type(value = GenericSyncObject.class, name = "MIXPANEL"),
        @JsonSubTypes.Type(value = GenericSyncObject.class, name = "RESTAPI")
})
=======
import lombok.*;

@Data
>>>>>>> a54fb5771e5bfbb1d2a38fb333193399cf240d4e
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SyncObject {

    private String objectName;
}
