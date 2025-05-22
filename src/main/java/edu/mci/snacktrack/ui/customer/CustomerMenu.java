package edu.mci.snacktrack.ui.customer;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import edu.mci.snacktrack.model.Dish;
import edu.mci.snacktrack.service.implementation.RestaurantService;

import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

@Route(value = "customer-menu/:restaurantId", layout = CustomerLayout.class)
@PageTitle("Customer Menu")
public class CustomerMenu extends VerticalLayout implements BeforeEnterObserver {

    private Long restaurantId;
    private final RestaurantService restaurantService;

    private List<Dish> allDishes;

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

        // Auskommentiert um Überschrift nicht zu formatieren
        //setAlignItems(Alignment.CENTER);
        //setJustifyContentMode(JustifyContentMode.CENTER);

        //add(new H2("Customer Menu"));
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        removeAll();

        restaurantService.findByIdWithMenu(restaurantId).ifPresentOrElse(restaurant -> {
            add(new H2("Menu for Restaurant: " + restaurant.getRestaurantName()));

            allDishes = restaurant.getMenu();

            if (allDishes == null || allDishes.isEmpty()) {
                add(new Paragraph("No dishes listed"));
            } else {
                TextField searchField = new TextField();
                searchField.setPlaceholder("Search dishes...");
                searchField.setClearButtonVisible(true);
                searchField.setWidth("300px");
                searchField.setValueChangeMode(ValueChangeMode.LAZY);

                // Layout for dishes
                HorizontalLayout dishLayout = new HorizontalLayout();
                dishLayout.setWrap(true);
                dishLayout.setWidthFull();
                dishLayout.setAlignItems(Alignment.CENTER);
                dishLayout.setJustifyContentMode(JustifyContentMode.CENTER);

                // Update view
                Runnable updateDishes = () -> {
                    String filter = searchField.getValue().trim().toLowerCase();

                    List<Dish> filtered = allDishes.stream()
                            .filter(d ->
                                    d.getDishName().toLowerCase().contains(filter) ||
                                    d.getCategory().stream()
                                            .anyMatch(cat -> cat.toLowerCase().contains(filter))
                            )
                            .collect(Collectors.toList());

                    dishLayout.removeAll();
                    filtered.forEach(dish -> dishLayout.add(new MenuViewCard(dish, true, false)));
                };

                updateDishes.run();

                // Listener on TextField
                searchField.addValueChangeListener(e -> updateDishes.run());

                add(searchField, dishLayout);
            }
        }, () -> {
            add(new H2("Restaurant not found"));
        });

    }

}
