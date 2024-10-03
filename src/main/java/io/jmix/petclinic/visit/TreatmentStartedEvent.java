package io.jmix.petclinic.visit;

import io.jmix.petclinic.entity.visit.Visit;
import org.springframework.context.ApplicationEvent;

// tag::treatment-started-event-definition[]
public class TreatmentStartedEvent extends ApplicationEvent {

    private final Visit visit;

    public TreatmentStartedEvent(Object source, Visit visit) {
        super(source);
        this.visit = visit;
    }

    public Visit getVisit() {
        return visit;
    }
}
// end::treatment-started-event-definition[]