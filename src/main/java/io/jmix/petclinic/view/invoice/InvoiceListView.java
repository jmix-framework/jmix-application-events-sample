package io.jmix.petclinic.view.invoice;

import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.*;
import io.jmix.petclinic.entity.invoice.Invoice;
import io.jmix.petclinic.view.main.MainView;


@Route(value = "invoices", layout = MainView.class)
@ViewController("petclinic_Invoice.list")
@ViewDescriptor("invoice-list-view.xml")
@LookupComponent("invoicesDataGrid")
@DialogMode(width = "64em")
public class InvoiceListView extends StandardListView<Invoice> {
}