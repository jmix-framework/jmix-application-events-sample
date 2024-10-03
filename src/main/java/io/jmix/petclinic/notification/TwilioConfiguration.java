package io.jmix.petclinic.notification;

import com.twilio.Twilio;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({
        TwilioProperties.class
})
public class TwilioConfiguration {
    private final static Logger log = LoggerFactory.getLogger(TwilioConfiguration.class);


    /**
     * Twilio Client needs to be initialised during startup of application
     * @param twilioProperties the twilio properties containing the credentials
     */
    @Autowired
    public TwilioConfiguration(TwilioProperties twilioProperties) {
        Twilio.init(
                twilioProperties.accountSid(),
                twilioProperties.authToken()
        );
        log.info("Twilio initialized ... with account sid {} ", twilioProperties.accountSid());
    }

}
