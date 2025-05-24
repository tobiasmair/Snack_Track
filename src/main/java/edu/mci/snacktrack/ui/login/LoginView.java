package edu.mci.snacktrack.ui.login;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.theme.lumo.Lumo;
import edu.mci.snacktrack.model.Customer;
import edu.mci.snacktrack.model.Restaurant;
import edu.mci.snacktrack.service.implementation.AuthService;
import edu.mci.snacktrack.ui.customer.CustomerHomeView;
import edu.mci.snacktrack.ui.restaurant.RestaurantHomeView;
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

        // Email field
        EmailField emailField = new EmailField("Email");
        emailField.setErrorMessage("This field is required");
        emailField.setErrorMessage("Enter a valid email address");
        emailField.setClearButtonVisible(true);

        // Password field
        PasswordField passwordField = new PasswordField("Password");
        passwordField.setErrorMessage("This field is required");

        // Buttons
        Button loginButton = new Button("Login");
        Button registerButton = new Button("Register");

        // error message
        Paragraph message = new Paragraph();
        message.setVisible(false);


        Paragraph or_text = new Paragraph("or");

        // login function
        loginButton.addClickListener(e -> {
            String email = emailField.getValue();
            String password = passwordField.getValue();
            String result = authService.authenticate(email, password);

            switch (result) {
                case "customer" -> {
                    Customer customer = authService.getCustomerByEmail(email);
                    VaadinSession.getCurrent().setAttribute("user", customer);
                    VaadinSession.getCurrent().setAttribute("userRole", "customer");
                    VaadinSession.getCurrent().setAttribute("userEmail", customer.getEmail());
                    UI.getCurrent().navigate(CustomerHomeView.class);
                }
                case "restaurant" -> {
                    Restaurant restaurant = authService.getRestaurantByEmail(email);
                    VaadinSession.getCurrent().setAttribute("user", restaurant);
                    VaadinSession.getCurrent().setAttribute("userRole", "restaurant");
                    VaadinSession.getCurrent().setAttribute("userEmail", restaurant.getEmail());
                    UI.getCurrent().navigate(RestaurantHomeView.class);
                }
                default -> {
                    message.setText(result);
                    message.setVisible(true);
                }
            }
        });

        registerButton.addClickListener(e -> UI.getCurrent().navigate("registration"));

        // Change between bright and dark Mode
        var themeToggle = new Checkbox("Bright theme");
        themeToggle.addValueChangeListener(e -> {
            setTheme(e.getValue());
        });

        add(title, emailField, passwordField, message, loginButton, or_text, registerButton, themeToggle);
    }

    // Change Theme
    private void setTheme(boolean light) {
        var js = "document.documentElement.setAttribute('theme', $0)";

        getElement().executeJs(js, light ? Lumo.LIGHT : Lumo.DARK);
    }
}