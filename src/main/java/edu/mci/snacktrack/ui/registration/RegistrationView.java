package edu.mci.snacktrack.ui.registration;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import edu.mci.snacktrack.model.Cuisine;
import edu.mci.snacktrack.model.Customer;
import edu.mci.snacktrack.model.Restaurant;
import edu.mci.snacktrack.service.implementation.CustomerService;
import edu.mci.snacktrack.service.implementation.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;

@Route("registration")
@PageTitle("Registration")
public class RegistrationView extends VerticalLayout {

    private final CustomerService customerService;
    private final RestaurantService restaurantService;

    @Autowired
    public RegistrationView(CustomerService customerService, RestaurantService restaurantService){

        this.customerService = customerService;
        this.restaurantService = restaurantService;

        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
        setSpacing(true);

        H2 title = new H2("Create Account");

        ComboBox<String> userTypeSelect = new ComboBox<>("Register as");
        userTypeSelect.setItems("Customer", "Restaurant");

        HorizontalLayout customerForm = createCustomerForm();
        HorizontalLayout restaurantForm = createRestaurantForm();

        Button backToLogin = new Button("Back to Login", e -> UI.getCurrent().navigate(""));

        // Initially hide both
        customerForm.setVisible(false);
        restaurantForm.setVisible(false);

        userTypeSelect.addValueChangeListener(event -> {
            String selected = event.getValue();
            customerForm.setVisible("Customer".equals(selected));
            restaurantForm.setVisible("Restaurant".equals(selected));
        });

        add(title, userTypeSelect, customerForm, restaurantForm, backToLogin);
    }


    private HorizontalLayout createCustomerForm() {

        TextField firstName = new TextField("First Name");
        firstName.setRequiredIndicatorVisible(true);
        firstName.setErrorMessage("This field is required");

        TextField lastName = new TextField("Last Name");
        lastName.setRequiredIndicatorVisible(true);
        lastName.setErrorMessage("This field is required");

        EmailField email = new EmailField("Email");
        email.setRequiredIndicatorVisible(true);
        email.setErrorMessage("Enter a valid email address");

        PasswordField password = new PasswordField("Password");
        password.setRequiredIndicatorVisible(true);
        password.setErrorMessage("This field is required");
        password.setClearButtonVisible(true);

        TextField address = new TextField("Address");

        Button register = new Button("Create Customer");

        Paragraph status = new Paragraph();
        status.setVisible(false);

        register.addClickListener(e -> {

            if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty()) {
                status.setText("First Name, Last Name, Email and Password are required.");
                status.setVisible(true);
                return;
            }


            try {
                Customer customer = customerService.createCustomer(
                        firstName.getValue(),
                        lastName.getValue(),
                        email.getValue(),
                        password.getValue(),
                        address.isEmpty() ? "-not set yet" : address.getValue()
                );

                register.setEnabled(false); // prevents multiple clicks

                status.setText("Customer created: " + customer.getFirstName() + " " + customer.getLastName() + " -> proceed to Login");
                status.setVisible(true);


            } catch (IllegalArgumentException ex){
                status.setText(ex.getMessage());
                status.setVisible(true);
                register.setEnabled(true);
            }
        });

        return new HorizontalLayout(firstName, lastName, email, password, address, register, status);
    }


    private HorizontalLayout createRestaurantForm() {
        TextField restaurantName = new TextField("Restaurant Name");
        restaurantName.setRequiredIndicatorVisible(true);
        restaurantName.setErrorMessage("This field is required");

        ComboBox<Cuisine> cuisineSelect = new ComboBox<>("Cuisine");
        cuisineSelect.setItems(Cuisine.values());
        cuisineSelect.setRequired(true);
        cuisineSelect.setErrorMessage("This field is required");

        EmailField email = new EmailField("Email");
        email.setRequiredIndicatorVisible(true);
        email.setErrorMessage("Enter a valid email address");

        PasswordField password = new PasswordField("Password");
        password.setRequiredIndicatorVisible(true);
        password.setErrorMessage("This field is required");
        password.setClearButtonVisible(true);

        TextField address = new TextField("Address");

        TextField vatNr = new TextField("VAT Number");

        Button register = new Button("Create Restaurant");

        Paragraph status = new Paragraph();
        status.setVisible(false);

        register.addClickListener(e -> {
            if (restaurantName.isEmpty() || cuisineSelect.isEmpty() || email.isEmpty() || password.isEmpty()) {
                status.setText("Name, Cuisine, Email and Password are required.");
                status.setVisible(true);
                return;
            }


            try {
            Restaurant restaurant = restaurantService.createRestaurant(
                    restaurantName.getValue(),
                    cuisineSelect.getValue(),
                    email.getValue(),
                    password.getValue(),
                    address.isEmpty() ? "-not set yet-" : address.getValue(),
                    vatNr.isEmpty() ? "-not set yet-" : vatNr.getValue()
            );

            register.setEnabled(false); // prevents multiple clicks

            status.setText("Restaurant created: " + restaurant.getRestaurantName() + " -> proceed to Login");
            status.setVisible(true);

            } catch (IllegalArgumentException ex){
                status.setText(ex.getMessage());
                status.setVisible(true);
                register.setEnabled(true);
            }
        });

        return new HorizontalLayout(restaurantName, cuisineSelect, email, password, address, vatNr, register, status);
    }
}
