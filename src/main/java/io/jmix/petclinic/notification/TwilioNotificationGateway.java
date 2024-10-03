package io.jmix.petclinic.notification;

import com.twilio.exception.ApiException;
import com.twilio.type.PhoneNumber;
import java.util.List;
import com.twilio.rest.api.v2010.account.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component(MobilePhoneNotificationGateway.NAME)
public class TwilioNotificationGateway implements MobilePhoneNotificationGateway {

    private static final Logger log = LoggerFactory.getLogger(TwilioNotificationGateway.class);

    private final TwilioProperties twilioProperties;

    public TwilioNotificationGateway(TwilioProperties twilioProperties) {
        this.twilioProperties = twilioProperties;
    }


    @Override
    public void sendNotification(String phoneNumber, String text) {

        try {
            Message message = Message.creator(
                    new PhoneNumber(phoneNumber),
                    new PhoneNumber(twilioProperties.fromPhoneNumber()),
                    text
            ).create();

            Message.Status messageStatus = message.getStatus();
            if (isDeliveredSuccessfully(messageStatus)) {
                log.info("Notification delivered successfully to {} (Message Status: {}). Twilio Reference ID: {}", phoneNumber, messageStatus, message.getSid());
            }
            else {
                log.error("Notification could not be send to " +  phoneNumber +  " via Twilio. Message Status: ", messageStatus);
            }
        } catch (ApiException e) {
            log.error("Notification could not be send to " +  phoneNumber +  " via Twilio.", e);
        }
    }

    private boolean isDeliveredSuccessfully(Message.Status messageStatus) {

        return List.of(
                Message.Status.ACCEPTED,
                Message.Status.DELIVERED,
                Message.Status.QUEUED,
                Message.Status.SENDING,
                Message.Status.SENT,
                Message.Status.RECEIVING,
                Message.Status.RECEIVED
        ).contains(messageStatus);
    }

}
