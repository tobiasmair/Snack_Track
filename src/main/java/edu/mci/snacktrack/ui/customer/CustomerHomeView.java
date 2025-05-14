package edu.mci.snacktrack.ui.customer;

import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.OrderedList;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.router.*;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.theme.lumo.LumoUtility;
import com.vaadin.flow.theme.lumo.LumoUtility.AlignItems;
import com.vaadin.flow.theme.lumo.LumoUtility.FontSize;
import com.vaadin.flow.theme.lumo.LumoUtility.JustifyContent;
import com.vaadin.flow.theme.lumo.LumoUtility.Margin;
import com.vaadin.flow.theme.lumo.LumoUtility.MaxWidth;
import com.vaadin.flow.theme.lumo.LumoUtility.Padding;
import edu.mci.snacktrack.model.Cuisine;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Route(value = "customer-home", layout = CustomerLayout.class)
@PageTitle("Customer Home")
public class CustomerHomeView extends VerticalLayout implements BeforeEnterObserver {

    private  OrderedList imageContainer;
    private List<GalleryViewCard> allCards = new ArrayList<>();


    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        String role = (String) VaadinSession.getCurrent().getAttribute("userRole");
        if (!"customer".equals(role)) {
            event.forwardTo("");
        }
    }

    public CustomerHomeView() {
        constructUI();

        allCards.add(new GalleryViewCard("Sakura Bites", "JAPANESE", "https://images.unsplash.com/photo-1553621042-f6e147245754?auto=format&fit=crop&w=800&q=80"));
        allCards.add(new GalleryViewCard("La Trattoria", "ITALIAN", "https://images.unsplash.com/photo-1498579150354-977475b7ea0b?q=80&w=2070&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"));
        allCards.add(new GalleryViewCard("Bombay Palace", "INDIAN", "https://images.unsplash.com/photo-1600891964599-f61ba0e24092?auto=format&fit=crop&w=800&q=80"));

    }

    private void constructUI() {
        addClassNames("customer-home"); // Platzhalter für CSS Klasse
        addClassNames(MaxWidth.SCREEN_LARGE, Margin.Horizontal.AUTO, Padding.Bottom.LARGE, Padding.Horizontal.LARGE);

        HorizontalLayout container = new HorizontalLayout();
        container.addClassNames(AlignItems.CENTER, JustifyContent.BETWEEN);

        VerticalLayout headerContainer = new VerticalLayout();
        H2 header = new H2("All Restaurants");
        header.addClassNames(Margin.Bottom.NONE, Margin.Top.XLARGE, FontSize.XXXLARGE);
        //Paragraph description = new Paragraph("");
        //description.addClassNames(Margin.Bottom.XLARGE, Margin.Top.NONE, TextColor.SECONDARY);
        headerContainer.add(header);

        Select<Object> cuisineFilter = new Select<>();
        cuisineFilter.setLabel("Cuisine");

        // Add "ALL"
        List<Object> cuisines = new ArrayList<>();
        cuisines.add("ALL");
        cuisines.addAll(Arrays.asList(Cuisine.values()));

        cuisineFilter.setItems(cuisines);
        cuisineFilter.setValue("ALL");

        cuisineFilter.addValueChangeListener(e -> updateGallery(e.getValue()));

        imageContainer = new OrderedList();
        imageContainer.addClassNames(LumoUtility.Gap.MEDIUM, LumoUtility.Display.GRID, LumoUtility.ListStyleType.NONE, LumoUtility.Margin.NONE, LumoUtility.Padding.NONE);

        container.add(headerContainer, cuisineFilter);
        add(container, imageContainer);

    }

    private void updateGallery(Object selected) {
        imageContainer.removeAll();
        for (GalleryViewCard card : allCards) {
            if ("All".equals(selected)) {
                imageContainer.add(card);
            } else if (selected instanceof Cuisine cuisine) {
                if (card.getCuisine().equalsIgnoreCase(cuisine.name())) {
                    imageContainer.add(card);
                }
            }
        }
    }


}
