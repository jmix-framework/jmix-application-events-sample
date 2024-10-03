package io.jmix.petclinic.visit;

import io.jmix.petclinic.entity.visit.Visit;
import org.springframework.context.ApplicationEvent;

public class TreatmentCompletedEvent extends ApplicationEvent {

    private final Visit visit;

    public TreatmentCompletedEvent(Object source, Visit visit) {
        super(source);
        this.visit = visit;
    }

    public Visit getVisit() {
        return visit;
    }
}