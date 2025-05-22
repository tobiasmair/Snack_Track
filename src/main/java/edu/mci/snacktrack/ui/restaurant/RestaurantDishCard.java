package edu.mci.snacktrack.ui.restaurant;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.NumberField;
import edu.mci.snacktrack.model.Dish;
import edu.mci.snacktrack.service.implementation.DishService;
import com.vaadin.flow.component.textfield.TextField;


public class RestaurantDishCard extends VerticalLayout {

    private final DishService dishService;
    private final Dish dish;

    public RestaurantDishCard(Dish dish, DishService dishService) {
        this.dish = dish;
        this.dishService = dishService;

        setWidth("320px");
        setMaxWidth("320px");
        getStyle().set("box-shadow", "0 2px 8px rgba(0,0,0,0.08)")
                .set("border-radius", "10px")
                .set("border", "1px solid #eee")
                .set("padding", "18px")
                .set("background", "#fff")
                .set("margin", "0.5rem 0");

        buildForm();
    }

    private void buildForm() {
        removeAll();

        H2 title = new H2(dish.getDishName());

        TextField dishName = new TextField("Dish Name");
        dishName.setValue(dish.getDishName() == null ? "" : dish.getDishName());
        dishName.setErrorMessage("This field is required");

        TextField dishDescription = new TextField("Dish Description");
        dishDescription.setValue(dish.getDishDescription() == null ? "" : dish.getDishDescription());

        NumberField dishPrice = new NumberField("Price");
        dishPrice.setMin(0);
        dishPrice.setErrorMessage("This field is required");
        dishPrice.setPlaceholder("Enter a decimal number");
        dishPrice.setValue(dish.getPrice());

        IntegerField dishCalories = new IntegerField("Calories");
        dishCalories.setMin(0);
        dishCalories.setPlaceholder("Enter an integer");
        dishCalories.setErrorMessage("Calories must be positive");
        dishCalories.setValue(dish.getCalories());

        IntegerField dishProtein = new IntegerField("Protein");
        dishProtein.setMin(0);
        dishProtein.setPlaceholder("Enter an integer");
        dishProtein.setErrorMessage("Protein must be positive");
        dishProtein.setValue(dish.getProtein());

        Button saveButton = new Button("Save Changes");

        saveButton.addClickListener(e -> {
            boolean changed = false;
            try {
                // Name
                if (!dishName.getValue().equals(dish.getDishName())) {
                    dishService.updateDishName(dish.getDishId(), dishName.getValue());
                    changed = true;
                }
                // Description
                if (!dishDescription.getValue().equals(dish.getDishDescription())) {
                    dishService.updateDishDescription(dish.getDishId(), dishDescription.getValue());
                    changed = true;
                }
                // Price
                if (!dishPrice.getValue().equals(dish.getPrice())) {
                    dishService.updateDishPrice(dish.getDishId(), dishPrice.getValue());
                    changed = true;
                }
                // Calories
                if (!dishCalories.getValue().equals(dish.getCalories())) {
                    dishService.updateDishCalories(dish.getDishId(), dishCalories.getValue());
                    changed = true;
                }
                // Protein
                if (!dishProtein.getValue().equals(dish.getProtein())) {
                    dishService.updateDishProtein(dish.getDishId(), dishProtein.getValue());
                    changed = true;
                }

                if (changed) {
                    Notification.show("Dish updated successfully.");
                    new Thread(() -> {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException ignored) {}
                        UI.getCurrent().access(() -> UI.getCurrent().getPage().reload());
                    }).start();
                } else {
                    Notification.show("No changes detected.");
                }
            } catch (IllegalArgumentException ex) {
                Notification.show("Error: " + ex.getMessage());
            }
        });

        add(title, dishName, dishDescription, dishPrice, dishCalories, dishProtein, saveButton);
    }
}
