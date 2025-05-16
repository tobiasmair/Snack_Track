package edu.mci.snacktrack.ui.customer;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import edu.mci.snacktrack.model.Dish;
import edu.mci.snacktrack.service.implementation.RestaurantService;

@Route(value = "customer-menu/:restaurantId", layout = CustomerLayout.class)
@PageTitle("Customer Menu")
public class CustomerMenu extends VerticalLayout implements BeforeEnterObserver {

    private Long restaurantId;
    private final RestaurantService restaurantService;

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        String role = (String) VaadinSession.getCurrent().getAttribute("userRole");
        if (!"customer".equals(role)) {
            event.forwardTo("");
        }

        String idParam = event.getRouteParameters().get("restaurantId").orElse(null);
        if (idParam == null) {
            event.forwardTo("customer-home");
            return;
        }

        try {
            restaurantId = Long.parseLong(idParam);
        } catch (NumberFormatException e) {
            event.forwardTo("customer-home");
        }
    }

    public CustomerMenu(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;

        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        //add(new H2("Customer Menu"));
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        removeAll();

        restaurantService.findByIdWithMenu(restaurantId).ifPresentOrElse(restaurant -> {
            add(new H2("Menu for Restaurant: " + restaurant.getRestaurantName()));

            if (restaurant.getMenu() == null || restaurant.getMenu().isEmpty()) {
                add(new Paragraph("No dishes listed"));
            } else {
                restaurant.getMenu().forEach(dish -> {
                    add(buildDishCard(dish));
                });
            }
        }, () -> {
            add(new H2("Restaurant not found"));
        });

    }

    private VerticalLayout buildDishCard(Dish dish) {
        VerticalLayout card = new VerticalLayout();
        card.addClassName("dish-card");
        card.add(new H2(dish.getDishName()));
        card.add(new Paragraph("Price: €" + dish.getPrice()));
        card.add(new Paragraph("Description: " + dish.getDishDescription()));
        return card;
    }
}
