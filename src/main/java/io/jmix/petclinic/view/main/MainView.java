package io.jmix.petclinic.view.main;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import io.jmix.core.DataManager;
import io.jmix.core.security.CurrentAuthentication;
import io.jmix.flowui.UiComponents;
import io.jmix.flowui.ViewNavigators;
import io.jmix.flowui.app.main.StandardMainView;
import io.jmix.flowui.component.main.JmixListMenu;
import io.jmix.flowui.facet.Timer;
import io.jmix.flowui.kit.component.main.ListMenu;
import io.jmix.flowui.view.*;
import io.jmix.petclinic.entity.visit.Visit;
import io.jmix.petclinic.online.OnlineDemoDataCreator;
import io.jmix.petclinic.view.visit.MyVisitsView;
import io.jmix.petclinic.visit.TreatmentStartedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;

// tag::treatment-started-event-consumer[]
@Route("")
@ViewController("MainView")
@ViewDescriptor("main-view.xml")
public class MainView extends StandardMainView {

    @ViewComponent
    private Span activeTreatments;
    @Autowired
    private ViewNavigators viewNavigators;

    @EventListener
    public void onTreatmentStarted(TreatmentStartedEvent event) {
        refreshActiveTreatmentCount();
    }

    private void refreshActiveTreatmentCount() {

        Long amount = calculateAmountOfActiveTreatments();
        activeTreatments.setText(messageBundle.formatMessage("activeTreatmentsBadge.text", amount));

        addActiveTreatmentPulseEffect();
    }

    private Long calculateAmountOfActiveTreatments() {
        return dataManager.loadValue("select count(e) from petclinic_Visit e " +
                                "where e.assignedNurse = :currentUser " +
                                "and e.treatmentStatus = @enum(io.jmix.petclinic.entity.visit.VisitTreatmentStatus.IN_PROGRESS)",
                        Long.class)
                .parameter("currentUser", currentAuthentication.getUser())
                .one();
    }
    // end::treatment-started-event-consumer[]


    @Autowired
    private UiComponents uiComponents;

    @ViewComponent
    private MessageBundle messageBundle;

    @Autowired
    private DataManager dataManager;

    @Autowired
    private CurrentAuthentication currentAuthentication;

    @Autowired(required = false)
    private OnlineDemoDataCreator onlineDemoDataCreator;

    @ViewComponent
    private JmixListMenu menu;

    @Subscribe
    public void onInit(final InitEvent event) {
        // TODO - replace when https://github.com/jmix-framework/jmix/issues/2213 is implemented
        Image image = uiComponents.create(Image.class);
        image.setSrc("images/petclinic_logo_with_slogan.svg");
        image.setClassName("login-image");
        image.setMaxWidth("50%");
        VerticalLayout verticalLayout = uiComponents.create(VerticalLayout.class);
        verticalLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        verticalLayout.add(image);
        verticalLayout.setHeightFull();
        verticalLayout.setWidthFull();
        getContent().showRouterLayoutContent(verticalLayout);

        initMyVisitBadge();
        refreshActiveTreatmentCount();
    }


    @Subscribe
    public void onReady(final ReadyEvent event) {
        if (onlineDemoDataCreator != null) {
            onlineDemoDataCreator.createDemoData();
        }

        registerActiveTreatmentRemoveCssClassOnAnimationEnd();
    }


    /*
    when animation ends the pulse CSS class is automatically removed again
     */
    private void registerActiveTreatmentRemoveCssClassOnAnimationEnd() {
        activeTreatments.getElement().executeJs(
                "this.addEventListener('animationend', function() { " +
                        "this.classList.remove('active-treatments-pulse'); });"
        );
    }

    /*
    we are using JS to add / remove CSS class for pulse effect, as we want to listen to JS event "animationend"
     */
    private void addActiveTreatmentPulseEffect() {
        activeTreatments.getElement().executeJs(
                "this.classList.add('active-treatments-pulse');"
        );
    }

    @Subscribe("refreshMyVisitsBadge")
    public void onRefreshMyVisitsBadgeTimerAction(final Timer.TimerActionEvent event) {
        ListMenu.MenuItem menuItem = menu.getMenuItem("petclinic_MyVisits");

        if (menuItem != null && menuItem.getSuffixComponent() instanceof Span badge) {
            badge.setText(messageBundle.formatMessage("myVisitMenuItemBadge.text", calculateAmountOfVisits()));
        }
    }

    private void initMyVisitBadge() {
        Span badge = uiComponents.create(Span.class);
        badge.setText(messageBundle.formatMessage("myVisitMenuItemBadge.text", calculateAmountOfVisits()));
        badge.getElement().getThemeList().add("badge warning");

        ListMenu.MenuItem menuItem = menu.getMenuItem("petclinic_MyVisits");
        if (menuItem != null) {
            menuItem.setSuffixComponent(badge);
        }
    }

    private long calculateAmountOfVisits() {
        return dataManager.loadValue("select count(e) from petclinic_Visit e " +
                                "where e.assignedNurse = :currentUser " +
                                "and e.treatmentStatus <> @enum(io.jmix.petclinic.entity.visit.VisitTreatmentStatus.DONE)",
                        Long.class)
                .parameter("currentUser", currentAuthentication.getUser())
                .one();
    }

    @Subscribe(id = "activeTreatments", subject = "clickListener")
    public void onActiveTreatmentsClick(final ClickEvent<Span> event) {
        viewNavigators.listView(this, Visit.class)
                .withViewClass(MyVisitsView.class)
                .navigate();
    }
}
