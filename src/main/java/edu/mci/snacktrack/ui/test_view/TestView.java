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
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

import edu.mci.snacktrack.service.implementation.CustomerService;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


/**
 * This view shows up when a user navigates to the root ('/') of the application.
 */
@Route("main")
public final class TestView extends Main {
    private final CustomerService customerService;
    private final RestaurantService restaurantService;
    private final DishService dishService;


    TestView(CustomerService customerService,
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

                PasswordField customerPasswordInput = new PasswordField("Password");
                customerPasswordInput.setRequiredIndicatorVisible(true);
                customerPasswordInput.setErrorMessage("This field is required");

                TextField customerAddressInput = new TextField("Address");
                customerAddressInput.setRequiredIndicatorVisible(true);
                customerAddressInput.setErrorMessage("This field is required");
                customerAddressInput.setClearButtonVisible(true);


                Button createCustomerButton = new Button("Create Customer");

                createCustomerButton.addClickListener(event -> {
                    String firstName = customerFirstNameInput.getValue();
                    String lastName = customerLastNameInput.getValue();
                    String email = customerEmailInput.getValue();
                    String password = customerPasswordInput.getValue();
                    String address = customerAddressInput.getValue();

                    Customer newCustomer = customerService.createCustomer(firstName, lastName, email, password, address);

                    System.out.println("Customer created: " + newCustomer);
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



        testAddCustomer.add(customerFirstNameInput, customerLastNameInput, customerEmailInput, customerPasswordInput, customerAddressInput, createCustomerButton);



        testAddDish.add(dishNameInput, dishDescriptionInput, dishPriceInput, dishCaloriesInput, dishProteinInput, dishCategoryInput, createDishButton);

        testCreateModels.add(testAddCustomer, testAddDish);

        add(testCreateModels);
    }

    /**
     * Navigates to the main view.
     */
    public static void showMainView() {
        UI.getCurrent().navigate(TestView.class);
    }
}