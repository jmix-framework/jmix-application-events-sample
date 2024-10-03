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
            Visit visit = loadVisit(event.getEntityId());
            tryToNotifyRoomSystemAboutVisit(visit);
        }
    }

    private Visit loadVisit(Id<Visit> visitId) {
        return dataManager
                .load(visitId)
                .joinTransaction(false)
                .one();
    }

    private void tryToNotifyRoomSystemAboutVisit(Visit visit) {
        roomSystemGateway.informAboutVisit(visit);
    }
}
// end::room-system-notifier[]
