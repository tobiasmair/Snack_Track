package edu.mci.snacktrack.ui.customer;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import edu.mci.snacktrack.model.Order;
import edu.mci.snacktrack.service.implementation.CustomerService;
import edu.mci.snacktrack.service.implementation.OrderService;

import java.util.List;

@Route(value = "customer-orders", layout = CustomerLayout.class)
@PageTitle("Customer Orders")
public class CustomerOrderHistory extends VerticalLayout implements BeforeEnterObserver {

    private final CustomerService customerService;
    private final OrderService orderService;

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        String role = (String) VaadinSession.getCurrent().getAttribute("userRole");
        if (!"customer".equals(role)) {
            event.forwardTo("");
        }
    }

    public CustomerOrderHistory(OrderService orderService, CustomerService customerService) {
        this.orderService = orderService;
        this.customerService = customerService;

        setSizeFull();
        //setAlignItems(Alignment.CENTER);
        //setJustifyContentMode(JustifyContentMode.CENTER);

        add(new H2("Customer Order History"));

        String email = (String) VaadinSession.getCurrent().getAttribute("userEmail");
        if (email == null) {
            add(new Paragraph("You are not logged in."));
            return;
        }

        customerService.getCustomerByEmail(email).ifPresentOrElse(customer -> {
            List<Order> orders = orderService.getOrderbyCustomer(customer);
            if (orders.isEmpty()) {
                add(new Paragraph("No orders found."));
            } else {
                HorizontalLayout orderHistoryLayout = new HorizontalLayout();
                orderHistoryLayout.setWrap(true);
                orderHistoryLayout.setWidthFull();
                orderHistoryLayout.setAlignItems(Alignment.START);
                orderHistoryLayout.setJustifyContentMode(JustifyContentMode.START);

                orders.forEach(order -> {
                    orderHistoryLayout.add(new OrderViewCard(order));
                });

                add(orderHistoryLayout);
            }
        }, () -> add(new Paragraph("Customer not found.")));
    }

}
