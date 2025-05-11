package edu.mci.snacktrack.ui.test_view;

import com.vaadin.flow.component.textfield.EmailField;
import edu.mci.snacktrack.model.Customer;
import edu.mci.snacktrack.model.Dish;
import edu.mci.snacktrack.model.Restaurant;
import edu.mci.snacktrack.service.implementation.DishService;
import edu.mci.snacktrack.service.implementation.RestaurantService;
import edu.mci.snacktrack.ui.component.ViewToolbar;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Main;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

import edu.mci.snacktrack.service.implementation.CustomerService;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


/**
 * This view shows up when a user navigates to the root ('/') of the application.
 */
@Route
public final class MainView extends Main {
    private final CustomerService customerService;
    private final RestaurantService restaurantService;
    private final DishService dishService;


    MainView(CustomerService customerService,
             RestaurantService restaurantService,
             DishService dishService) {

        this.customerService = customerService;
        this.restaurantService = restaurantService;
        this.dishService = dishService;

        addClassName(LumoUtility.Padding.MEDIUM);
        add(new ViewToolbar("Test"));
        add(new Div("This is a view for testing database logic with UI interaction."));


        VerticalLayout testCreateModels = new VerticalLayout();
        testCreateModels.setSpacing(true); // adds space between components
        testCreateModels.setPadding(false); // optional: no internal padding
        testCreateModels.setMargin(false);  // optional: no margin around layout

            // Creating Customer
            HorizontalLayout testAddCustomer = new HorizontalLayout();
            testAddCustomer.setSpacing(true); // adds space between components
            testAddCustomer.setPadding(false); // optional: no internal padding
            testAddCustomer.setMargin(false);  // optional: no margin around layout

                TextField customerFirstNameInput = new TextField("First Name");
                customerFirstNameInput.setRequiredIndicatorVisible(true);
                customerFirstNameInput.setErrorMessage("This field is required");
                customerFirstNameInput.setClearButtonVisible(true);

                TextField customerLastNameInput = new TextField("Last Name");
                customerLastNameInput.setRequiredIndicatorVisible(true);
                customerLastNameInput.setErrorMessage("This field is required");
                customerLastNameInput.setClearButtonVisible(true);


                EmailField customerEmailInput = new EmailField("Email");
                customerEmailInput.setRequiredIndicatorVisible(true);
                customerEmailInput.setErrorMessage("Enter a valid email address");
                customerEmailInput.setClearButtonVisible(true);

                TextField customerAddressInput = new TextField("Address");
                customerAddressInput.setRequiredIndicatorVisible(true);
                customerAddressInput.setErrorMessage("This field is required");
                customerAddressInput.setClearButtonVisible(true);


                Button createCustomerButton = new Button("Create Customer");

                createCustomerButton.addClickListener(event -> {
                    String firstName = customerFirstNameInput.getValue();
                    String lastName = customerLastNameInput.getValue();
                    String email = customerEmailInput.getValue();
                    String address = customerAddressInput.getValue();

                    Customer newCustomer = customerService.createCustomer(firstName, lastName, email, address);

                    System.out.println("Customer created: " + newCustomer);
                });

        // Creating Restaurant
        HorizontalLayout testAddRestaurant = new HorizontalLayout();
        testAddRestaurant.setSpacing(true); // adds space between components
        testAddRestaurant.setPadding(false); // optional: no internal padding
        testAddRestaurant.setMargin(false);  // optional: no margin around layout

            TextField restaurantNameInput = new TextField("Restaurant Name");
            restaurantNameInput.setRequiredIndicatorVisible(true);
            restaurantNameInput.setErrorMessage("This field is required");
            restaurantNameInput.setClearButtonVisible(true);

            TextField restaurantCuisineInput = new TextField("Cuisine");
            restaurantCuisineInput.setRequiredIndicatorVisible(true);
            restaurantCuisineInput.setErrorMessage("This field is required");
            restaurantCuisineInput.setClearButtonVisible(true);

            TextField restaurantAddressInput = new TextField("Address");
            restaurantAddressInput.setRequiredIndicatorVisible(true);
            restaurantAddressInput.setErrorMessage("This field is required");
            restaurantAddressInput.setClearButtonVisible(true);

            EmailField restaurantEmailInput = new EmailField("Email");
            restaurantEmailInput.setRequiredIndicatorVisible(true);
            restaurantEmailInput.setErrorMessage("Enter a valid email address");
            restaurantEmailInput.setClearButtonVisible(true);

            TextField restaurantVatNrInput = new TextField("Vat. Number");
            restaurantVatNrInput.setRequiredIndicatorVisible(true);
            restaurantVatNrInput.setErrorMessage("This field is required");
            restaurantVatNrInput.setClearButtonVisible(true);

            Button createRestaurantButton = new Button("Create Restaurant");

            createRestaurantButton.addClickListener(event -> {
                String restaurantName = restaurantNameInput.getValue();
                String cuisine = restaurantCuisineInput.getValue();
                String address = restaurantAddressInput.getValue();
                String email = restaurantEmailInput.getValue();
                String varNr = restaurantVatNrInput.getValue();

                Restaurant newRestaurant = restaurantService.createRestaurant(restaurantName, cuisine, address, email, varNr);

                System.out.println("Restaurant created: " + newRestaurant);
            });

        // Creating Dish
        HorizontalLayout testAddDish = new HorizontalLayout();
        testAddDish.setSpacing(true); // adds space between components
        testAddDish.setPadding(false); // optional: no internal padding
        testAddDish.setMargin(false);  // optional: no margin around layout

            TextField dishNameInput = new TextField("Dish Name");
            dishNameInput.setRequiredIndicatorVisible(true);
            dishNameInput.setErrorMessage("This field is required");
            dishNameInput.setClearButtonVisible(true);

            TextField dishDescriptionInput = new TextField("Dish Description");
            dishDescriptionInput.setRequiredIndicatorVisible(true);
            dishDescriptionInput.setErrorMessage("This field is required");
            dishDescriptionInput.setClearButtonVisible(true);

            NumberField dishPriceInput = new NumberField("Price");
            dishPriceInput.setMin(0);     // prevent negative values
            dishPriceInput.setPlaceholder("Enter a decimal number");

            IntegerField dishCaloriesInput = new IntegerField("Calories");
            dishCaloriesInput.setMin(0); //  prevent negative values
            dishCaloriesInput.setPlaceholder("Enter an integer");

            IntegerField dishProteinInput = new IntegerField("Protein");
            dishProteinInput.setMin(0); //  prevent negative values
            dishProteinInput.setPlaceholder("Enter an integer");

            TextField dishCategoryInput = new TextField("Categories (separated by ';')");
            dishCategoryInput.setPlaceholder("e.g. Vegan;Starter");
            dishCategoryInput.setClearButtonVisible(true);


            Button createDishButton = new Button("Create Dish");

            createDishButton.addClickListener(event -> {
                String dishName = dishNameInput.getValue();
                String dishDescription = dishDescriptionInput.getValue();
                double price = dishPriceInput.getValue();
                int calories = dishCaloriesInput.getValue();
                int protein = dishProteinInput.getValue();
                String rawCategories = dishCategoryInput.getValue();
                List<String> categories = Arrays.stream(rawCategories.split(";"))
                        .map(String::trim)       // Trim spaces
                        .filter(s -> !s.isEmpty()) // Remove empty entries
                        .collect(Collectors.toList());


                Dish newDish = dishService.createDish(dishName, dishDescription, price, calories, protein, categories);

                System.out.println("Dish created: " + newDish);
            });



        testAddCustomer.add(customerFirstNameInput, customerLastNameInput, customerEmailInput, customerAddressInput, createCustomerButton);

        testAddRestaurant.add(restaurantNameInput, restaurantCuisineInput, restaurantEmailInput, restaurantAddressInput, restaurantVatNrInput, createRestaurantButton);

        testAddDish.add(dishNameInput, dishDescriptionInput, dishPriceInput, dishCaloriesInput, dishProteinInput, dishCategoryInput, createDishButton);

        testCreateModels.add(testAddCustomer, testAddRestaurant, testAddDish);

        add(testCreateModels);
    }

    /**
     * Navigates to the main view.
     */
    public static void showMainView() {
        UI.getCurrent().navigate(MainView.class);
    }
}