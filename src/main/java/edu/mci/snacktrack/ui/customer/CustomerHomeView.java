package edu.mci.snacktrack.ui.customer;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.router.*;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.theme.lumo.LumoUtility.AlignItems;
import com.vaadin.flow.theme.lumo.LumoUtility.FontSize;
import com.vaadin.flow.theme.lumo.LumoUtility.JustifyContent;
import com.vaadin.flow.theme.lumo.LumoUtility.Margin;
import com.vaadin.flow.theme.lumo.LumoUtility.MaxWidth;
import com.vaadin.flow.theme.lumo.LumoUtility.Padding;
import edu.mci.snacktrack.model.Cuisine;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import edu.mci.snacktrack.service.implementation.RestaurantService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Route(value = "customer-home", layout = CustomerLayout.class)
@PageTitle("Customer Home")
public class CustomerHomeView extends VerticalLayout implements BeforeEnterObserver {

    private  HorizontalLayout restaurantContainer;
    private List<GalleryViewCard> allCards = new ArrayList<>();
    private final RestaurantService restaurantService;

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        String role = (String) VaadinSession.getCurrent().getAttribute("userRole");
        if (!"customer".equals(role)) {
            event.forwardTo("");
        }
    }

    public CustomerHomeView(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
        constructUI();

        restaurantService.getAllRestaurants().forEach(restaurant -> {
            String imageUrl = "https://images.unsplash.com/photo-1498654896293-37aacf113fd9?q=80&w=1740&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D";

            // Load ViewCard for every Restaurant in the DB
            GalleryViewCard card = new GalleryViewCard(
                    restaurant,
                    imageUrl
            );
            allCards.add(card);
            card.addClickListener(event -> {
                UI.getCurrent().navigate("customer-menu/" + restaurant.getRestaurantId());
            });
        });

        updateGallery("ALL");

    }

    private void constructUI() {
        addClassNames("customer-home"); // Platzhalter für CSS Klasse
        addClassNames(MaxWidth.SCREEN_LARGE, Margin.Horizontal.AUTO, Padding.Bottom.LARGE, Padding.Horizontal.LARGE);

        HorizontalLayout container = new HorizontalLayout();
        container.addClassNames(AlignItems.CENTER, JustifyContent.BETWEEN);

        VerticalLayout headerContainer = new VerticalLayout();
        H2 header = new H2("All Restaurants");
        header.addClassNames(Margin.Bottom.NONE, Margin.Top.XLARGE, FontSize.XXXLARGE);
        headerContainer.add(header);

        Select<Object> cuisineFilter = new Select<>();
        cuisineFilter.setLabel("Cuisine");

        // Add Cuisine Filter "ALL"
        List<Object> cuisines = new ArrayList<>();
        cuisines.add("ALL");
        cuisines.addAll(Arrays.asList(Cuisine.values()));

        cuisineFilter.setItems(cuisines);
        cuisineFilter.setValue("ALL");

        restaurantContainer = new HorizontalLayout();
        restaurantContainer.setWidthFull();
        restaurantContainer.setWrap(true);
        restaurantContainer.setAlignItems(Alignment.CENTER);
        restaurantContainer.setJustifyContentMode(JustifyContentMode.CENTER);

        cuisineFilter.addValueChangeListener(e -> updateGallery(e.getValue()));

        container.add(headerContainer, cuisineFilter);
        add(container, restaurantContainer);

    }

    private void updateGallery(Object selected) {
        restaurantContainer.removeAll();

        for (GalleryViewCard card : allCards) {
            if ("ALL".equals(selected)) {
                restaurantContainer.add(card);
            } else if (selected instanceof Cuisine cuisine) {
                if (card.getCuisine().equalsIgnoreCase(cuisine.name())) {
                    restaurantContainer.add(card);
                }
            }
        }
    }

}
