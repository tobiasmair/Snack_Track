package edu.mci.snacktrack.ui.restaurant;


import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
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
import edu.mci.snacktrack.model.Cuisine;
import edu.mci.snacktrack.model.Restaurant;
import edu.mci.snacktrack.service.implementation.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import edu.mci.snacktrack.ui.component.ConfirmDialog;

@Route(value = "restaurant-profile", layout = RestaurantLayout.class)
@PageTitle("Restaurant Profile")
public class RestaurantProfile extends VerticalLayout implements BeforeEnterObserver {

    private final RestaurantService restaurantService;
    private Restaurant currentRestaurant;

    @Autowired
    public RestaurantProfile(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;

        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        String role = (String) VaadinSession.getCurrent().getAttribute("userRole");
        if (!"restaurant".equals(role)) {
            event.forwardTo("");
            return;
        }

        String email = (String) VaadinSession.getCurrent().getAttribute("userEmail");
        if (email == null) {
            Notification.show("No user logged in.");
            event.forwardTo("");
            return;
        }

        restaurantService.getRestaurantByEmail(email).ifPresentOrElse(restaurant -> {
            this.currentRestaurant = restaurant;
            buildForm(restaurant);
        }, () -> {
            Notification.show("Restaurant not found.");
            event.forwardTo("");
        });
    }

    private void buildForm(Restaurant restaurant) {
        removeAll();

        H2 title = new H2("Edit Restaurant Profile");

        TextField restaurantName = new TextField("Restaurant Name");
        restaurantName.setValue(restaurant.getRestaurantName());

        ComboBox<Cuisine> cuisineSelect = new ComboBox<>("Cuisine");
        cuisineSelect.setItems(Cuisine.values());
        cuisineSelect.setValue(restaurant.getCuisine());

        EmailField emailField = new EmailField("Email");
        emailField.setValue(restaurant.getEmail());
        emailField.setErrorMessage("Enter a valid email address");

        PasswordField passwordField = new PasswordField("New Password (leave empty to keep current)");
        passwordField.setClearButtonVisible(true);

        TextField addressField = new TextField("Address");
        addressField.setValue(restaurant.getAddress() != null ? restaurant.getAddress() : "");

        TextField vatNrField = new TextField("VAT Number");
        vatNrField.setValue(restaurant.getVatNr() != null ? restaurant.getVatNr() : "");

        Button updateButton = new Button("Edit Profile");

        updateButton.addClickListener(e -> {
            try {
                boolean changed = false;

                if (!restaurantName.getValue().equals(restaurant.getRestaurantName())) {
                    restaurantService.updateRestaurantName(restaurant.getRestaurantId(), restaurantName.getValue());
                    changed = true;
                }

                // Cuisine: update only if changed
                Cuisine selectedCuisine = cuisineSelect.getValue();
                if (selectedCuisine != null && !selectedCuisine.equals(restaurant.getCuisine())) {
                    restaurantService.updateRestaurantCuisine(restaurant.getRestaurantId(), selectedCuisine);
                    changed = true;
                }

                if (!emailField.getValue().equals(restaurant.getEmail())) {
                    restaurantService.updateRestaurantEmail(restaurant.getRestaurantId(), emailField.getValue());
                    changed = true;
                }

                if (!passwordField.isEmpty()) {
                    restaurantService.updateRestaurantPassword(restaurant.getRestaurantId(), passwordField.getValue());
                    changed = true;
                }

                if (!addressField.getValue().equals(restaurant.getAddress())) {
                    restaurantService.updateRestaurantAddress(restaurant.getRestaurantId(), addressField.getValue());
                    changed = true;
                }

                if (!vatNrField.getValue().equals(restaurant.getVatNr())) {
                    restaurantService.updateRestaurantVatNr(restaurant.getRestaurantId(), vatNrField.getValue());
                    changed = true;
                }

                if (changed) {
                    Notification.show("Profile updated successfully.");
                    new Thread(() -> {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException ignored) {}
                        // reload Page
                        UI.getCurrent().getPage().reload();
                    }).start();

                } else {
                    Notification.show("No changes detected.");
                }

            } catch (IllegalArgumentException ex) {
                Notification.show("Error: " + ex.getMessage());
            }

        });

        Button deleteButton = new Button("Delete Restaurant");
        deleteButton.getStyle()
                .set("background-color", "#ffeaea")
                .set("color", "#c00")
                .set("margin-top", "1.2rem");

        deleteButton.addClickListener(e -> {
            ConfirmDialog confirmDialog = new ConfirmDialog(
                    "Are you sure you want to delete this restaurant? This action cannot be undone.",
                    () -> {
                        restaurantService.softDeleteRestaurant(restaurant.getRestaurantId());
                        Notification.show("Restaurant deleted.");
                        // Optionally log out user and redirect to login:
                        VaadinSession.getCurrent().close();
                        UI.getCurrent().navigate("login");
                    },
                    null
            );
            confirmDialog.open();
        });

        add(title, restaurantName, cuisineSelect, emailField, passwordField, addressField, vatNrField, updateButton, deleteButton);
    }

}
