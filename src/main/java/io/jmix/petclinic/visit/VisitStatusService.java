package io.jmix.petclinic.visit;

import io.jmix.core.DataManager;
import io.jmix.petclinic.entity.visit.Visit;
import io.jmix.petclinic.entity.visit.VisitTreatmentStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component("petclinic_VisitStatusService")
public class VisitStatusService {

    private static final Logger log = LoggerFactory.getLogger(VisitStatusService.class);


    // tag::finish-treatment[]
    private final DataManager dataManager;
    private final ApplicationEventPublisher applicationEventPublisher;

    public VisitStatusService(DataManager dataManager, ApplicationEventPublisher applicationEventPublisher) {
        this.dataManager = dataManager;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    public void finishTreatment(Visit visit) {
        markVisitAsDone(visit);
        applicationEventPublisher.publishEvent(new TreatmentCompletedEvent(this, visit));
    }
    // end::finish-treatment[]

    private void markVisitAsDone(Visit visit) {
        visit.setTreatmentStatus(VisitTreatmentStatus.DONE);
        dataManager.save(visit);
        log.info("Visit {} marked as done", visit);
    }

    public void startVisit(Visit visit) {
        if (!visit.getTreatmentStatus().equals(VisitTreatmentStatus.UPCOMING)) {
            log.info("Visit {} is not in status upcoming. Starting visit is not possible.", visit);
        }

        visit.setTreatmentStatus(VisitTreatmentStatus.IN_PROGRESS);
        dataManager.save(visit);
    }
}