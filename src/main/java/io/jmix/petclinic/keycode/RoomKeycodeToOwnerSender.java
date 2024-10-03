package io.jmix.petclinic.keycode;

import io.jmix.core.DataManager;
import io.jmix.core.FetchPlan;
import io.jmix.core.Id;
import io.jmix.core.Messages;
import io.jmix.core.event.EntityChangedEvent;
import io.jmix.petclinic.entity.owner.Owner;
import io.jmix.petclinic.entity.pet.Pet;
import io.jmix.petclinic.entity.visit.Visit;
import io.jmix.petclinic.notification.MobilePhoneNotificationGateway;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.Optional;

// tag::room-keycode-to-owner-sender[]
@Component
public class RoomKeycodeToOwnerSender {

    private final DataManager dataManager;
    private final Messages messages;
    private final MobilePhoneNotificationGateway mobilePhoneNotificationGateway;

    public RoomKeycodeToOwnerSender(DataManager dataManager, Messages messages, MobilePhoneNotificationGateway mobilePhoneNotificationGateway) {
        this.dataManager = dataManager;
        this.messages = messages;
        this.mobilePhoneNotificationGateway = mobilePhoneNotificationGateway;
    }

    @TransactionalEventListener
    public void sendRoomKeycode(EntityChangedEvent<Visit> event) {
        if (event.getType().equals(EntityChangedEvent.Type.CREATED)) {
            Visit visit = loadVisit(event.getEntityId());
            tryToSendRoomKeycodeToPetsOwner(visit);
        }
    }

    private Visit loadVisit(Id<Visit> visitId) {
        return dataManager
                .load(visitId)
                .joinTransaction(false)
                .fetchPlan(visit -> {
                    visit.addFetchPlan(FetchPlan.BASE);
                    visit.add("room", FetchPlan.BASE);
                    visit.add("pet", pet -> {
                        pet.addFetchPlan(FetchPlan.BASE);
                        pet.add("owner", FetchPlan.BASE);
                    });
                })
                .one();
    }

    private void tryToSendRoomKeycodeToPetsOwner(Visit visit) {
        Optional.ofNullable(visit.getPet())
                .map(Pet::getOwner)
                .map(Owner::getTelephone)
                .ifPresent(phoneNumber ->
                        mobilePhoneNotificationGateway.sendNotification(phoneNumber, createNotificationText(visit))
                );
    }

    private String createNotificationText(Visit visit) {
        return messages.formatMessage(
                this.getClass(),
                "roomKeycodeNotification",
                visit.getPet().getName(),
                visit.getRoom().getRoomNumber(),
                visit.getRoom().getName(),
                visit.getRoomKeycode()
        );
    }
}
// end::room-keycode-to-owner-sender[]
