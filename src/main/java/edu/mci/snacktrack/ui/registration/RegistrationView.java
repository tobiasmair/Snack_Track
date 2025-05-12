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

        TextField lastName = new TextField("Last Name");
        lastName.setRequiredIndicatorVisible(true);

        EmailField email = new EmailField("Email");
        email.setRequiredIndicatorVisible(true);

        PasswordField password = new PasswordField("Password");
        password.setRequiredIndicatorVisible(true);

        TextField address = new TextField("Address");
        address.setRequiredIndicatorVisible(true);

        Button register = new Button("Create Customer");

        Paragraph status = new Paragraph();
        status.setVisible(false);

        register.addClickListener(e -> {
            if (email.isEmpty() || password.isEmpty()) {
                status.setText("Email and password are required.");
                status.setVisible(true);
                return;
            }

            Customer customer = customerService.createCustomer(
                    firstName.getValue(),
                    lastName.getValue(),
                    email.getValue(),
                    password.getValue(),
                    address.getValue()
            );

            status.setText("Customer created: " + customer.getFirstName() + " " + customer.getLastName());
            status.setVisible(true);
        });

        return new HorizontalLayout(firstName, lastName, email, password, address, register, status);
    }


    private HorizontalLayout createRestaurantForm() {
        TextField name = new TextField("Restaurant Name");
        name.setRequiredIndicatorVisible(true);

        TextField cuisine = new TextField("Cuisine");
        cuisine.setRequiredIndicatorVisible(true);

        EmailField email = new EmailField("Email");
        email.setRequiredIndicatorVisible(true);

        PasswordField password = new PasswordField("Password");
        password.setRequiredIndicatorVisible(true);

        TextField address = new TextField("Address");
        address.setRequiredIndicatorVisible(true);

        TextField vatNr = new TextField("VAT Number");
        vatNr.setRequiredIndicatorVisible(true);

        Button register = new Button("Create Restaurant");

        Paragraph status = new Paragraph();
        status.setVisible(false);

        register.addClickListener(e -> {
            if (email.isEmpty() || password.isEmpty()) {
                status.setText("Email and password are required.");
                status.setVisible(true);
                return;
            }

            Restaurant restaurant = restaurantService.createRestaurant(
                    name.getValue(),
                    cuisine.getValue(),
                    email.getValue(),
                    password.getValue(),
                    address.getValue(),
                    vatNr.getValue()
            );

            status.setText("Restaurant created: " + restaurant.getRestaurantName());
            status.setVisible(true);
        });

        return new HorizontalLayout(name, cuisine, email, password, address, vatNr, register, status);
    }
}
