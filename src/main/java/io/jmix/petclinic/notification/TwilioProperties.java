package io.jmix.petclinic.notification;

import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;


/**
 * Twilio properties containing API credentials
 * @param fromPhoneNumber the phone number used by Twilio to send out the SMS
 * @param accountSid the Account SID from Twilio
 * @param authToken the Auth Token from Twilio
 */
@Validated
@ConfigurationProperties(prefix = "twilio")
public record TwilioProperties(
        @NotNull
        String fromPhoneNumber,
        @NotNull
        String accountSid,
        @NotNull
        String authToken
) {
}
