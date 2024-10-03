package io.jmix.petclinic.keycode;


import io.jmix.petclinic.entity.visit.Visit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


/**
 * Simulation of a gateway responsible for interacting with the actual room management system
 */
@Component
public class RoomSystemGateway {

    private static final Logger log = LoggerFactory.getLogger(RoomSystemGateway.class);

    void informAboutVisit(Visit visit) {
        log.info(
                "Sending visit: {} to the Room System to notify about the room keycode for {}",
                visit.getId(),
                visit.getVisitStart()
        );
    }
}
