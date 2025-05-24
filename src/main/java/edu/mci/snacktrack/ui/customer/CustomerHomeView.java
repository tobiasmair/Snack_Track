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

        // Show only active Restaurants
        restaurantService.getAllActiveRestaurants().forEach(restaurant -> {
            String imageUrl = getImageUrlForCuisine(restaurant.getCuisine());

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

    // Different Basic Images for different cuisines
    private String getImageUrlForCuisine(Cuisine cuisine) {
        return switch (cuisine) {
            case ITALIAN -> "https://images.unsplash.com/photo-1498579150354-977475b7ea0b?q=80&w=2070&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D";
            case AUSTRIAN -> "https://plus.unsplash.com/premium_photo-1693879090564-4617efcd0f0b?q=80&w=1976&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D";
            case CHINESE -> "https://plus.unsplash.com/premium_photo-1661600135596-dcb910b05340?q=80&w=2071&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D";
            case JAPANESE -> "https://images.unsplash.com/photo-1553621042-f6e147245754";
            case THAI -> "https://plus.unsplash.com/premium_photo-1695936035134-05c844086f24?q=80&w=2070&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D";
            case KOREAN -> "https://images.unsplash.com/photo-1661366394743-fe30fe478ef7?q=80&w=2070&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D";
            case INDIAN -> "https://images.unsplash.com/photo-1585937421612-70a008356fbe?q=80&w=1936&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D";
            case GREEK -> "https://images.unsplash.com/photo-1555939594-58d7cb561ad1?q=80&w=1974&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D";
            case MEXICAN -> "https://images.unsplash.com/photo-1565299585323-38d6b0865b47?q=80&w=1980&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D";
            case AMERICAN -> "https://images.unsplash.com/photo-1586190848861-99aa4a171e90";
        };
    }


}
