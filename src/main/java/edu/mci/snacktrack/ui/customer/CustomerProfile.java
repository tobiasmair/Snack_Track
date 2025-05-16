package edu.mci.snacktrack.ui.customer;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import edu.mci.snacktrack.model.Customer;
import edu.mci.snacktrack.service.implementation.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;


@Route(value = "customer-profile", layout = CustomerLayout.class)
@PageTitle("Customer Profile")
public class CustomerProfile extends VerticalLayout implements BeforeEnterObserver {


    private final CustomerService customerService;
    private Customer currentCustomer;

    @Autowired
    public CustomerProfile(CustomerService customerService) {
        this.customerService = customerService;

        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        String role = (String) VaadinSession.getCurrent().getAttribute("userRole");
        System.out.println("Session userRole: " + role); // Debug

        if (!"customer".equals(role)) {
            System.out.println("Role not valid -> forwarding to login");
            event.forwardTo("");
            return;
        }



    String email = (String) VaadinSession.getCurrent().getAttribute("userEmail");
        if (email == null) {
        Notification.show("No user logged in.");
        event.forwardTo("");
        return;
    }

        customerService.getCustomerByEmail(email).ifPresentOrElse(customer -> {
        this.currentCustomer = customer;
        buildForm(customer);
    }, () -> {
        Notification.show("Customer not found.");
        event.forwardTo("");
    });
}

    private void buildForm(Customer customer) {
        removeAll();

        H2 title = new H2("Edit Customer Profile");

        TextField firstName = new TextField("First Name");
        firstName.setValue(customer.getFirstName());

        TextField lastName = new TextField("Last Name");
        lastName.setValue(customer.getLastName());

        EmailField emailField = new EmailField("Email");
        emailField.setValue(customer.getEmail());

        PasswordField passwordField = new PasswordField("New Password (leave empty to keep current)");
        passwordField.setClearButtonVisible(true);

        TextField addressField = new TextField("Address");
        addressField.setValue(customer.getAddress());

        Button updateButton = new Button("Edit Profile");

        updateButton.addClickListener(e -> {
            try {
                boolean changed = false;

                if (!firstName.getValue().equals(customer.getFirstName())) {
                    customerService.updateCustomerFirstName(customer.getId(), firstName.getValue());
                    changed = true;
                }

                if (!lastName.getValue().equals(customer.getLastName())) {
                    customerService.updateCustomerLastName(customer.getId(), lastName.getValue());
                    changed = true;
                }

                if (!emailField.getValue().equals(customer.getEmail())) {
                    customerService.updateCustomerEmail(customer.getId(), emailField.getValue());
                    changed = true;
                }

                if (!passwordField.isEmpty()) {
                    customerService.updateCustomerPassword(customer.getId(), passwordField.getValue());
                    changed = true;
                }

                if (!addressField.getValue().equals(customer.getAddress())) {
                    customerService.updateCustomerAddress(customer.getId(), addressField.getValue());
                    changed = true;
                }

                if (changed) {
                    Notification.show("Profile updated successfully.");
                    // Optional: refresh customer object here
                } else {
                    Notification.show("No changes detected.");
                }

            } catch (IllegalArgumentException ex) {
                Notification.show("Error: " + ex.getMessage());
            }
        });

        add(title, firstName, lastName, emailField, passwordField, addressField, updateButton);
    }

}
