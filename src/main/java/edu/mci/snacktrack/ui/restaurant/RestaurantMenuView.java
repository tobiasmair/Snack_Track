package edu.mci.snacktrack.ui.restaurant;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.*;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import edu.mci.snacktrack.model.Dish;
import edu.mci.snacktrack.model.Restaurant;
import edu.mci.snacktrack.service.implementation.DishService;
import edu.mci.snacktrack.ui.customer.MenuViewCard;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Route(value = "restaurant-menu", layout = RestaurantLayout.class)
@PageTitle("Restaurant Menu")
public class RestaurantMenuView extends VerticalLayout implements BeforeEnterObserver {

    private final DishService dishService;
    private final Div dishesScrollContainer = new Div();
    private final Div emptyMenuMessage = new Div();

    private HorizontalLayout createDishForm; // The form layout
    private Button showFormButton;           // The "Add a dish" button

    public RestaurantMenuView(@Autowired DishService dishService) {
        this.dishService = dishService;

        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        add(new H2("Menu Overview"));

        // create button to show form
        showFormButton = new Button("Add a dish", event -> toggleFormVisibility());
        add(showFormButton);

        // create the form (initially hidden)
        createDishForm = createDishForm();
        createDishForm.setVisible(false);  // Hide at start
        add(createDishForm);

        // scrollable container for dish-cards
        dishesScrollContainer.setWidth("100%");
        dishesScrollContainer.setHeight("60vh"); // or any height you prefer
        dishesScrollContainer.getStyle()
                .set("display", "flex")
                .set("flex-wrap", "wrap")
                .set("overflow-y", "auto")
                .set("gap", "1.5rem")
                .set("padding", "1rem")
                .set("justify-content", "center"); // Optional: center cards

        add(dishesScrollContainer);

        emptyMenuMessage.setText("Menu is empty → Create a dish!");
        emptyMenuMessage.getStyle()
                .set("font-size", "1.3rem")
                .set("color", "#888")
                .set("margin", "2rem auto")
                .set("text-align", "center");

        emptyMenuMessage.setVisible(false); // hidden at first
        add(emptyMenuMessage);

        refreshDishList();
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        String role = (String) VaadinSession.getCurrent().getAttribute("userRole");
        if (!"restaurant".equals(role)) {
            event.forwardTo("");
        }
    }

    // Displays all current dishes for the restaurant
    private void refreshDishList() {
        dishesScrollContainer.removeAll();
        Restaurant restaurant = (Restaurant) VaadinSession.getCurrent().getAttribute("user");
        if (restaurant == null) return;

        List<Dish> dishes = dishService.findByRestaurant(restaurant)
                .stream()
                .filter(Dish::isActive)
                .collect(Collectors.toList());

        if (dishes.isEmpty()) {
            emptyMenuMessage.setVisible(true);
        } else {
            emptyMenuMessage.setVisible(false);
            for (Dish dish : dishes) {
                dishesScrollContainer.add(new RestaurantDishCard(dish, dishService));
            }
        }
    }

    // Dish creation form
    private HorizontalLayout createDishForm() {
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
        dishCalories.setMin(0);
        dishCalories.setPlaceholder("Enter an integer");
        dishCalories.setErrorMessage("Calories must be positive");

        IntegerField dishProtein = new IntegerField("Protein");
        dishProtein.setMin(0);
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

            String rawCategories = dishCategories.getValue();
            List<String> categories = Arrays.stream(rawCategories.split(";"))
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .collect(Collectors.toList());

            Restaurant restaurant = (Restaurant) VaadinSession.getCurrent().getAttribute("user");
            if (restaurant == null) {
                showNotification("Restaurant session error.", createDishButton);
                return;
            }

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
                refreshDishList(); // Refresh the dish list after adding a new dish

            } catch (IllegalArgumentException ex) {
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

        return new HorizontalLayout(
                dishName, dishDescription, dishPrice, dishCalories,
                dishProtein, dishCategories, createDishButton
        );
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

    // helper method to toggle the addDish form
    private void toggleFormVisibility() {
        boolean visible = !createDishForm.isVisible();
        createDishForm.setVisible(visible);
        if (visible) {
            showFormButton.setText("Cancel");
        } else {
            showFormButton.setText("Add a dish");
        }
    }
}
