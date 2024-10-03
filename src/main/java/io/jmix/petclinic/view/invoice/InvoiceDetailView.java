package io.jmix.petclinic.view.invoice;

import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.EditedEntityContainer;
import io.jmix.flowui.view.StandardDetailView;
import io.jmix.flowui.view.ViewController;
import io.jmix.flowui.view.ViewDescriptor;
import io.jmix.petclinic.entity.invoice.Invoice;
import io.jmix.petclinic.view.main.MainView;

@Route(value = "invoices/:id", layout = MainView.class)
@ViewController("petclinic_Invoice.detail")
@ViewDescriptor("invoice-detail-view.xml")
@EditedEntityContainer("invoiceDc")
public class InvoiceDetailView extends StandardDetailView<Invoice> {
}