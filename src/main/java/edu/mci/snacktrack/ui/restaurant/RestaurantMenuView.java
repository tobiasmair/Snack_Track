package edu.mci.snacktrack.ui.restaurant;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
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
        add(new H2("Welcome, Restaurant!"));

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

        Paragraph status = new Paragraph();
        status.setVisible(false);

        createDishButton.addClickListener(e -> {

            if (dishName.isEmpty() || dishPrice.isEmpty()) {
                status.setText("Dish Name and Price are required.");
                status.setVisible(true);
                return;
            }

            if (dishPrice.getValue() < 0) {
                status.setText("Price must be positive.");
                status.setVisible(true);
                return;
            }

            if (!dishCalories.isEmpty() && dishCalories.getValue() < 0) {
                status.setText("Calories must be positive.");
                status.setVisible(true);
                return;
            }

            if (!dishProtein.isEmpty() && dishProtein.getValue() < 0) {
                status.setText("Protein must be positive.");
                status.setVisible(true);
                return;
            }

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

                status.setText("Dish " + "'" + newDish.getDishName() + "'" + " created!");
                status.setVisible(true);


            } catch (IllegalArgumentException ex){
                status.setText(ex.getMessage());
                status.setVisible(true);
            }
        });


        return new HorizontalLayout(dishName, dishDescription, dishPrice, dishCalories, dishProtein, dishCategories, createDishButton, status);
    }
}
