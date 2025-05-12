package edu.mci.snacktrack.ui.login;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import edu.mci.snacktrack.model.Customer;
import edu.mci.snacktrack.model.Restaurant;
import edu.mci.snacktrack.service.implementation.AuthService;
import edu.mci.snacktrack.ui.customer.CustomerHomeView;
import edu.mci.snacktrack.ui.restaurant.RestaurantHomeView;
import com.vaadin.flow.component.html.Paragraph;
import org.springframework.beans.factory.annotation.Autowired;

@Route("")
@PageTitle("Login")
public class LoginView extends VerticalLayout {

    private final AuthService authService;

    @Autowired
    public LoginView(AuthService authService) {
        this.authService = authService;

        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        H1 title = new H1("Welcome to SnackTrack!");

        EmailField emailField = new EmailField("Email");
        emailField.setRequiredIndicatorVisible(true);
        emailField.setErrorMessage("This field is required");
        emailField.setErrorMessage("Enter a valid email address");
        emailField.setClearButtonVisible(true);

        PasswordField passwordField = new PasswordField("Password");
        passwordField.setRequiredIndicatorVisible(true);
        passwordField.setErrorMessage("This field is required");
        Button loginButton = new Button("Login");
        Paragraph message = new Paragraph();
        message.setVisible(false);

        loginButton.addClickListener(e -> {
            String email = emailField.getValue();
            String password = passwordField.getValue();
            String result = authService.authenticate(email, password);

            switch (result) {
                case "customer" -> {
                    Customer customer = authService.getCustomerByEmail(email);
                    VaadinSession.getCurrent().setAttribute("user", customer);
                    VaadinSession.getCurrent().setAttribute("userRole", "customer");
                    UI.getCurrent().navigate(CustomerHomeView.class);
                }
                case "restaurant" -> {
                    Restaurant restaurant = authService.getRestaurantByEmail(email);
                    VaadinSession.getCurrent().setAttribute("user", restaurant);
                    VaadinSession.getCurrent().setAttribute("userRole", "restaurant");
                    UI.getCurrent().navigate(RestaurantHomeView.class);
                }
                default -> {
                    message.setText(result);
                    message.setVisible(true);
                }
            }
        });

        add(title, emailField, passwordField, loginButton, message);
    }
}