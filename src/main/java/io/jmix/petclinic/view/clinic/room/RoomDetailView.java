package io.jmix.petclinic.view.clinic.room;

import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.EditedEntityContainer;
import io.jmix.flowui.view.StandardDetailView;
import io.jmix.flowui.view.ViewController;
import io.jmix.flowui.view.ViewDescriptor;
import io.jmix.petclinic.entity.room.Room;
import io.jmix.petclinic.view.main.MainView;

@Route(value = "rooms/:id", layout = MainView.class)
@ViewController("petclinic_Room.detail")
@ViewDescriptor("room-detail-view.xml")
@EditedEntityContainer("roomDc")
public class RoomDetailView extends StandardDetailView<Room> {
}