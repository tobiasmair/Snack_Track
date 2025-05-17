package edu.mci.snacktrack.ui.restaurant;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.*;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import edu.mci.snacktrack.model.BasketSession;
import edu.mci.snacktrack.model.Dish;
import edu.mci.snacktrack.model.Restaurant;
import edu.mci.snacktrack.service.implementation.DishService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


@Route(value = "restaurant-menu", layout = RestaurantLayout.class)
@PageTitle("Restaurant Menu")
public class RestaurantMenuView extends VerticalLayout implements BeforeEnterObserver {

    private final DishService dishService;


    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        String role = (String) VaadinSession.getCurrent().getAttribute("userRole");
        if (!"restaurant".equals(role)) {
            event.forwardTo("");
        }
    }

    @Autowired
    public RestaurantMenuView(DishService dishService) {

        this.dishService = dishService;

        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
        add(new H2("Menu Overview"));

        HorizontalLayout dishForm = createDishForm();

        add(dishForm);
    }

    private HorizontalLayout createDishForm() {
        Restaurant restaurant = (Restaurant) VaadinSession.getCurrent().getAttribute("user");


        TextField dishName = new TextField("Dish Name");
        dishName.setRequiredIndicatorVisible(true);
        dishName.setErrorMessage("This field is required");
        dishName.setClearButtonVisible(true);

        TextField dishDescription = new TextField("Dish Description");
        dishDescription.setClearButtonVisible(true);

        NumberField dishPrice = new NumberField("Price");
        dishPrice.setRequiredIndicatorVisible(true);
        dishPrice.setMin(0);     // prevent negative values
        dishPrice.setErrorMessage("This field is required");
        dishPrice.setPlaceholder("Enter a decimal number");

        IntegerField dishCalories = new IntegerField("Calories");
        dishCalories.setMin(0); //  prevent negative values
        dishCalories.setPlaceholder("Enter an integer");
        dishCalories.setErrorMessage("Calories must be positive");

        IntegerField dishProtein = new IntegerField("Protein");
        dishProtein.setMin(0); //  prevent negative values
        dishProtein.setPlaceholder("Enter an integer");
        dishProtein.setErrorMessage("Protein must be positive");

        TextField dishCategories = new TextField("Categories (separated by ';')");
        dishCategories.setPlaceholder("e.g. Vegan;Starter");
        dishCategories.setClearButtonVisible(true);

        Button createDishButton = new Button("Create Dish");

        createDishButton.addClickListener(e -> {

            createDishButton.setEnabled(false);

            if (dishName.isEmpty() || dishPrice.isEmpty()) {
                showNotification("Dish Name and Price are required.", createDishButton);
                return;
            }

            if (dishPrice.getValue() < 0) {
                showNotification("Price must be a positive value.", createDishButton);
                return;
            }

            if (!dishCalories.isEmpty() && dishCalories.getValue() < 0) {
                showNotification("Calories must be a positive value.", createDishButton);
                return;
            }

            if (!dishProtein.isEmpty() && dishProtein.getValue() < 0) {
                showNotification("Protein must be a positive value.", createDishButton);
                return;
            }

            // parsing categories
            String rawCategories = dishCategories.getValue();
            List<String> categories = Arrays.stream(rawCategories.split(";"))
                    .map(String::trim)       // Trim spaces
                    .filter(s -> !s.isEmpty()) // Remove empty entries
                    .collect(Collectors.toList());

            try {
                Dish newDish = dishService.createDish(
                        dishName.getValue(),
                        dishDescription.isEmpty() ? "-not set yet-" : dishDescription.getValue(),
                        dishPrice.getValue(),
                        dishCalories.isEmpty() ? 0 : dishCalories.getValue(),
                        dishProtein.isEmpty() ? 0 : dishProtein.getValue(),
                        categories,
                        restaurant
                );
                showNotification("Dish '" + newDish.getDishName() + "' created!", createDishButton);


            } catch (IllegalArgumentException ex){
                showNotification(ex.getMessage(), createDishButton);
            }

            // Enable button after timer
            UI ui = UI.getCurrent();
            new Thread(() -> {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException ignored) {}
                ui.access(() -> createDishButton.setEnabled(true));
            }).start();
        });


        return new HorizontalLayout(dishName, dishDescription, dishPrice, dishCalories, dishProtein, dishCategories, createDishButton);
    }


    // helper method to show notification and enable button
    private void showNotification(String message, Button enableButton) {
        Notification.show(message, 3000, Notification.Position.MIDDLE);

        // Re-enable button after delay
        UI ui = UI.getCurrent();
        new Thread(() -> {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException ignored) {}
            ui.access(() -> enableButton.setEnabled(true));
        }).start();
    }

}
