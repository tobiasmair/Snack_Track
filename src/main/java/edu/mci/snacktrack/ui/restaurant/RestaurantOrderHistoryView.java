package edu.mci.snacktrack.ui.restaurant;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import edu.mci.snacktrack.model.Order;
import edu.mci.snacktrack.model.Restaurant;
import edu.mci.snacktrack.service.implementation.OrderService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Route(value = "restaurant-completed-orders", layout = RestaurantLayout.class)
@PageTitle("Restaurant Completed Orders")
public class RestaurantOrderHistoryView extends VerticalLayout implements BeforeEnterObserver {

    private final OrderService orderService;
    private final Div ordersScrollContainer = new Div();

    public RestaurantOrderHistoryView(@Autowired OrderService orderService) {
        this.orderService = orderService;

        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        add(new H2("Completed Orders"));

        // Set up the scrollable container for orders
        ordersScrollContainer.setWidth("100%");
        ordersScrollContainer.getStyle()
                .set("max-height", "60vh")
                .set("overflow-y", "auto")
                .set("padding", "1rem")
                .set("display", "flex")
                .set("flex-wrap", "wrap")
                .set("justify-content", "center")
                .set("gap", "1rem");
        add(ordersScrollContainer);
        setFlexGrow(1, ordersScrollContainer);

        refreshOrderHistoryList();
    }


    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        String role = (String) VaadinSession.getCurrent().getAttribute("userRole");
        if (!"restaurant".equals(role)) {
            event.forwardTo("");
        }
    }

    private void refreshOrderHistoryList() {
        ordersScrollContainer.removeAll();
        Restaurant restaurant = (Restaurant) VaadinSession.getCurrent().getAttribute("user");
        if (restaurant == null) return;

        List<Order> openOrders = orderService.getPastOrdersByRestaurant(restaurant);
        if (openOrders.isEmpty()) {
            Div emptyMsg = new Div();
            emptyMsg.setText("No completed orders yet.");
            emptyMsg.getStyle()
                    .set("font-size", "1.3rem")
                    .set("color", "#888")
                    .set("margin", "2rem auto")
                    .set("text-align", "center");
            ordersScrollContainer.add(emptyMsg);
            return;
        }

        for (Order order : openOrders) {
            ordersScrollContainer.add(new RestaurantOrderCard(order, orderService));
        }
    }

}