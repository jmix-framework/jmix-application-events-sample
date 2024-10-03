package io.jmix.petclinic.invoice;

import io.jmix.core.DataManager;
import io.jmix.core.SaveContext;
import io.jmix.data.Sequence;
import io.jmix.data.Sequences;
import io.jmix.petclinic.entity.invoice.Invoice;
import io.jmix.petclinic.entity.invoice.InvoiceItem;
import io.jmix.petclinic.entity.visit.Visit;
import io.jmix.petclinic.visit.TreatmentCompletedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

// tag::create-visit-invoice[]
@Component("petclinic_invoicingProcessInitializer")
public class InvoicingProcessInitializer {
    private static final Logger log = LoggerFactory.getLogger(InvoicingProcessInitializer.class);

    private final DataManager dataManager;
    private final Sequences sequences;

    public InvoicingProcessInitializer(DataManager dataManager, Sequences sequences) {
        this.dataManager = dataManager;
        this.sequences = sequences;
    }

    @EventListener
    public void onTreatmentCompleted(TreatmentCompletedEvent event) {
        log.info("Invoicing process initialized: {}", event.getVisit());

        SaveContext saveContext = new SaveContext();
        createInvoiceFor(event.getVisit(), saveContext);

        dataManager.save(saveContext);
    }

    private void createInvoiceFor(Visit visit, SaveContext saveContext) {
        Invoice invoice = dataManager.create(Invoice.class);

        invoice.setVisit(visit);
        invoice.setInvoiceDate(visit.getVisitEnd().toLocalDate());
        invoice.setInvoiceNumber(createInvoiceNumber());
        saveContext.saving(invoice);

        createInvoiceItemsFor(invoice)
                .forEach(saveContext::saving);
    }

    private List<InvoiceItem> createInvoiceItemsFor(Invoice invoice) {
        InvoiceItem invoiceItem = dataManager.create(InvoiceItem.class);

        invoiceItem.setInvoice(invoice);
        invoiceItem.setPosition(1);
        invoiceItem.setText("Visit flat fee");
        invoiceItem.setPrice(new BigDecimal("150.0"));

        return List.of(invoiceItem);
    }

    private String createInvoiceNumber() {
        return String.format("%04d", sequences.createNextValue(Sequence.withName("vists")));
    }
}
// end::create-visit-invoice[]
