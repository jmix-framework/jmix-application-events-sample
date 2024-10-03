package io.jmix.petclinic.keycode;

import io.jmix.core.DataManager;
import io.jmix.core.Id;
import io.jmix.core.event.EntityChangedEvent;
import io.jmix.petclinic.entity.visit.Visit;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

// tag::room-system-notifier[]
@Component
public class RoomSystemNotifier {

    private final DataManager dataManager;
    private final RoomSystemGateway roomSystemGateway;

    public RoomSystemNotifier(DataManager dataManager, RoomSystemGateway roomSystemGateway) {
        this.dataManager = dataManager;
        this.roomSystemGateway = roomSystemGateway;
    }

    @TransactionalEventListener
    public void notifyRoomSystem(EntityChangedEvent<Visit> event) {
        if (event.getType().equals(EntityChangedEvent.Type.CREATED)) {
            roomSystemGateway.informAboutVisit(
                    loadVisit(event.getEntityId())
            );
        }
    }

    private Visit loadVisit(Id<Visit> visitId) {
        return dataManager
                .load(visitId)
                .joinTransaction(false)
                .one();
    }
}
// end::room-system-notifier[]
