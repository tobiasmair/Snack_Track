package edu.mci.snacktrack.ui.customer;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;
import com.vaadin.flow.server.VaadinSession;
import edu.mci.snacktrack.model.*;
import edu.mci.snacktrack.service.implementation.CustomerService;
import edu.mci.snacktrack.service.implementation.OrderService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Route(value = "customer-basket", layout = CustomerLayout.class)
@PageTitle("Customer Basket")
public class CustomerBasket extends VerticalLayout implements BeforeEnterObserver {

    private final OrderService orderService;
    private final CustomerService customerService;

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        String role = (String) VaadinSession.getCurrent().getAttribute("userRole");
        if (!"customer".equals(role)) {
            event.forwardTo("");
        }
    }

    @Autowired
    public CustomerBasket(OrderService orderService, CustomerService customerService) {
        this.orderService = orderService;
        this.customerService = customerService;

        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        // Title
        H2 title = new H2("Your Basket");
        title.getStyle().set("margin-bottom", "20px");
        add(title);

        // Fetch basket items from session
        List<Dish> basketDishes = BasketSession.getBasket();

        if (basketDishes.isEmpty()) {
            add(new H2("Your basket is empty"));
        } else {
            Map<Dish, Long> dishCounts = BasketSession.getDishCounts();

            // Scrollable container for dish cards
            Div dishesScrollContainer = new Div();
            dishesScrollContainer.setWidth("100%");
            dishesScrollContainer.getStyle()
                    .set("max-height", "60vh")
                    .set("overflow-y", "auto")
                    .set("padding", "1rem")
                    .set("display", "flex")
                    .set("flex-wrap", "wrap")
                    .set("justify-content", "center")
                    .set("gap", "1rem");

            dishCounts.forEach((dish, qty) -> {
                MenuViewCard dishCard = new MenuViewCard(dish, false, true);
                dishCard.setQuantity(qty);
                dishesScrollContainer.add(dishCard);
            });

            add(dishesScrollContainer);
            setFlexGrow(1, dishesScrollContainer);

            // Calculate Sum of order
            double totalPrice = basketDishes.stream()
                    .mapToDouble(Dish::getPrice)
                    .sum();

            int totalCalories = basketDishes.stream()
                    .mapToInt(Dish::getCalories)
                    .sum();

            Paragraph totalPriceParagraph = new Paragraph("Total Price: €" + String.format("%.2f", totalPrice));
            Paragraph totalCaloriesParagraph = new Paragraph("Total Calories: " + totalCalories + " kcal");
            totalPriceParagraph.getStyle().set("font-weight", "bold");
            totalCaloriesParagraph.getStyle().set("font-weight", "bold");

            add(totalPriceParagraph, totalCaloriesParagraph);
        }

        // Checkout Basket Button
        Button checkoutButton = new Button("Checkout");
        checkoutButton.getStyle().set("margin-bottom", "20px");

        checkoutButton.addClickListener(event -> {
            List<Dish>currentBasket = BasketSession.getBasket();
            System.out.println("Current basket size: " + currentBasket.size());


            if (currentBasket.isEmpty()) {
                Notification.show("Basket is empty", 3000, Notification.Position.MIDDLE);
                return;
            } else {
                String email = (String) VaadinSession.getCurrent().getAttribute("userEmail");

                if (email == null) {
                    Notification.show("No user logged in.");
                    UI.getCurrent().navigate("");
                    return;
                }

                try {
                    Customer customer = customerService.getCustomerByEmail(email)
                            .orElseThrow(() -> new RuntimeException("Customer not found"));

                    // Get the restaurant from the first dish (all dishes from same restaurant)
                    Restaurant restaurant = BasketSession.getRestaurant();

                    Notification.show("CustomerId: " + customer.getCustomerId(), 3000, Notification.Position.MIDDLE);

                    if (restaurant == null) {
                        throw new IllegalStateException("Dish does not have a restaurant associated.");
                    }

                    // Create Order
                    Order createOrder = orderService.createOrder(currentBasket, customer, restaurant);

                    // Clear Basket
                    BasketSession.clearBasket();

                    Notification.show("Order #" + createOrder.getOrderId() + "created!", 3000, Notification.Position.MIDDLE);

                    UI.getCurrent().getPage().reload();

                } catch (Exception e) {
                    Notification.show("Failed to checkout basket " + e.getMessage(), 3000, Notification.Position.MIDDLE);
                }
            }
        });

        // Clear Basket Button
        Button clearBasketButton = new Button("Clear Basket");
        checkoutButton.getStyle().set("margin-bottom", "20px");

        clearBasketButton.addClickListener(event -> {
            BasketSession.clearBasket();
            UI.getCurrent().getPage().reload();
        });

        add(checkoutButton, clearBasketButton);

    }

}
