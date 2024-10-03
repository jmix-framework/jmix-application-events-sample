package io.jmix.petclinic.view.clinic.room;

import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.*;
import io.jmix.petclinic.entity.room.Room;
import io.jmix.petclinic.view.main.MainView;


@Route(value = "rooms", layout = MainView.class)
@ViewController("petclinic_Room.list")
@ViewDescriptor("room-list-view.xml")
@LookupComponent("roomsDataGrid")
@DialogMode(width = "64em")
public class RoomListView extends StandardListView<Room> {
}