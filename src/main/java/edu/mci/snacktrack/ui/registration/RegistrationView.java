package edu.mci.snacktrack.ui.registration;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
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


        register.addClickListener(e -> {

            register.setEnabled(false); // prevents multiple clicks

            if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty()) {
                showNotification("First Name, Last Name, Email and Password are required.", register);
                return;
            }


            try {
                Customer customer = customerService.createCustomer(
                        firstName.getValue(),
                        lastName.getValue(),
                        email.getValue(),
                        password.getValue(),
                        address.isEmpty() ? "-not set yet-" : address.getValue()
                );

                register.setEnabled(false); // prevents multiple clicks

                showNotificationAndRedirect(
                        "Customer created: " + customer.getFirstName() + " " + customer.getLastName() + " -> redirecting to Login...",
                        register,
                        ""
                );


            } catch (IllegalArgumentException ex){
                showNotification(ex.getMessage(), register);
            }
        });

        return new HorizontalLayout(firstName, lastName, email, password, address, register);
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


        register.addClickListener(e -> {
            register.setEnabled(false); // prevents multiple clicks


            if (restaurantName.isEmpty() || cuisineSelect.isEmpty() || email.isEmpty() || password.isEmpty()) {
                showNotification("Name, Cuisine, Email and Password are required.", register);
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


                showNotificationAndRedirect(
                        "Restaurant created: " + restaurant.getRestaurantName() + " -> redirecting to Login...",
                        register,
                        ""
                );

            } catch (IllegalArgumentException ex){
                showNotification(ex.getMessage(), register);
            }
        });

        return new HorizontalLayout(restaurantName, cuisineSelect, email, password, address, vatNr, register);
    }

    // helper method to show notification and enable button
    private void showNotification(String message, Button enableButton) {
        Notification.show(message, 3000, Notification.Position.MIDDLE);
        UI ui = UI.getCurrent();
        new Thread(() -> {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException ignored) {}
            ui.access(() -> enableButton.setEnabled(true));
        }).start();
    }

    // helper method to show notification and redirect to login page
    private void showNotificationAndRedirect(String message, Button enableButton, String targetRoute) {
        Notification.show(message, 3000, Notification.Position.MIDDLE);
        UI ui = UI.getCurrent();
        new Thread(() -> {
            try {
                Thread.sleep(3000); // 3-second delay
            } catch (InterruptedException ignored) {}
            ui.access(() -> ui.navigate(targetRoute)); // redirect after delay
        }).start();
    }
}
