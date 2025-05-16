package edu.mci.snacktrack.ui.customer;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import edu.mci.snacktrack.model.BasketSession;
import edu.mci.snacktrack.model.Dish;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Route(value = "customer-basket2", layout = CustomerLayout.class)
@PageTitle("Customer Basket2")
public class CustomerBasket2 extends VerticalLayout implements BeforeEnterObserver {

    public CustomerBasket2() {
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        // Title
        H2 title = new H2("Your Basket");
        title.getStyle().set("margin-bottom", "20px");
        add(title);

        // Fetch basket items from session
        List<Dish> basketDishes = BasketSession.getBasket();

        if (basketDishes.isEmpty()) {
            // Empty state
            add(new H2("Your basket is empty"));
        } else {
            // Group identical dishes and count quantities
            Map<Dish, Integer> counts = basketDishes.stream()
                    .collect(Collectors.toMap(
                            dish -> dish,
                            dish -> 1,
                            Integer::sum,
                            LinkedHashMap::new
                    ));

            // Layout for dish cards
            FlexLayout dishLayout = new FlexLayout();
            dishLayout.getStyle().set("flex-wrap", "wrap");
            dishLayout.getStyle().set("gap", "20px");
            dishLayout.setWidthFull();

            // Create a card for each unique dish, setting its quantity
            counts.forEach((dish, qty) -> {
                MenuViewCard2 dishCard = new MenuViewCard2(dish, false);
                dishCard.setQuantity(qty);
                dishLayout.add(dishCard);
            });

            add(dishLayout);
        }

        // Checkout button
        Button checkoutButton = new Button("Checkout");
        checkoutButton.getStyle().set("margin-bottom", "20px");
        add(checkoutButton);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        // Ensure only customers can access this view
        String role = (String) VaadinSession.getCurrent().getAttribute("userRole");
        if (!"customer".equals(role)) {
            event.forwardTo("");
        }
    }
}
