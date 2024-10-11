package io.jmix.petclinic.view.visit;

import com.vaadin.flow.router.Route;
import io.jmix.flowui.component.combobox.EntityComboBox;
import io.jmix.flowui.view.*;
import io.jmix.petclinic.EmployeeRepository;
import io.jmix.petclinic.entity.User;
import io.jmix.petclinic.entity.visit.Visit;
import io.jmix.petclinic.view.main.MainView;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Random;


// tag::generate-room-keycode[]
@Route(value = "visits/:id", layout = MainView.class)
@ViewController("petclinic_Visit.detail")
@ViewDescriptor("visit-detail-view.xml")
@EditedEntityContainer("visitDc")
public class VisitDetailView extends StandardDetailView<Visit> {

    @Subscribe
    protected void onInitEntity(InitEntityEvent<Visit> event) {
        event.getEntity().setRoomKeycode(generateRoomKeycode());
    }

    private String generateRoomKeycode() {
        int rookKeycode = new Random().nextInt(999999);
        return String.format("%04d", rookKeycode);
    }
    // end::generate-room-keycode[]

    @Autowired
    private EmployeeRepository employeeRepository;
    @ViewComponent
    private EntityComboBox<User> assignedNurseField;


    @Subscribe
    public void onInit(final InitEvent event) {
        assignedNurseField.setItems(employeeRepository.findAllNurses());
    }

}
