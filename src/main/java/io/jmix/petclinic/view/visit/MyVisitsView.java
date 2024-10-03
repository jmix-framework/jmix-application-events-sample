package io.jmix.petclinic.view.visit;

import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.Notifications;
import io.jmix.flowui.UiEventPublisher;
import io.jmix.flowui.component.grid.DataGrid;
import io.jmix.flowui.kit.action.ActionPerformedEvent;
import io.jmix.flowui.view.*;
import io.jmix.petclinic.entity.visit.Visit;
import io.jmix.petclinic.view.main.MainView;
import io.jmix.petclinic.visit.TreatmentStartedEvent;
import io.jmix.petclinic.visit.VisitStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;

@Route(value = "my-visits", layout = MainView.class)
@ViewController("petclinic_MyVisits")
@ViewDescriptor("my-visits-view.xml")
@LookupComponent("visitsDataGrid")
@DialogMode(width = "64em")
public class MyVisitsView extends StandardListView<Visit> {

    @ViewComponent
    private DataGrid<Visit> visitsDataGrid;
    @Autowired
    private Notifications notifications;
    @ViewComponent
    private MessageBundle messageBundle;
    @Autowired
    private VisitStatusService visitStatusService;

    // tag::treatment-started-event-producer[]
    @Autowired
    private UiEventPublisher uiEventPublisher;

    @Subscribe("visitsDataGrid.startTreatment")
    public void onStartTreatment(final ActionPerformedEvent event) {
        Visit visit = visitsDataGrid.getSingleSelectedItem();
        if (visit == null)
            return;

        if (visit.hasStarted()) {
            notifications.create(messageBundle.formatMessage("treatmentAlreadyStarted", visit.getPetName()))
                    .withType(Notifications.Type.WARNING)
                    .show();
            return;
        }

        visitStatusService.startVisit(visit);

        uiEventPublisher.publishEventForCurrentUI(new TreatmentStartedEvent(this, visit));
    }
    // end::treatment-started-event-producer[]

    @EventListener
    public void onTreatmentStarted(TreatmentStartedEvent event) {
        getViewData().loadAll();
        notifications.create(messageBundle.formatMessage("treatmentStarted", event.getVisit().getPetName()))
                .withType(Notifications.Type.SUCCESS)
                .withPosition(Notification.Position.BOTTOM_END)
                .show();
    }

    @Subscribe("visitsDataGrid.finishTreatment")
    public void onFinishTreatment(final ActionPerformedEvent event) {
        Visit visit = visitsDataGrid.getSingleSelectedItem();
        if (visit == null)
            return;

        if (visit.hasFinished()) {
            notifications.create(messageBundle.formatMessage("treatmentAlreadyFinished", visit.getPetName()))
                    .withType(Notifications.Type.WARNING)
                    .show();
            return;
        }

        visitStatusService.finishTreatment(visit);
        getViewData().loadAll();
        notifications.create(messageBundle.formatMessage("treatmentFinished", visit.getPetName()))
                .withType(Notifications.Type.SUCCESS)
                .withPosition(Notification.Position.BOTTOM_END)
                .show();
    }
}
