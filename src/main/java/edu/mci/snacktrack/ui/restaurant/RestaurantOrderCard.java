package edu.mci.snacktrack.ui.restaurant;

import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import edu.mci.snacktrack.model.Dish;
import edu.mci.snacktrack.model.Order;
import edu.mci.snacktrack.model.OrderStatus;
import edu.mci.snacktrack.service.implementation.OrderService;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class RestaurantOrderCard extends VerticalLayout {

    private final Span statusBadge = new Span();
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private final OrderService orderService; // add this as a field

    public RestaurantOrderCard(Order order, OrderService orderService) {

        this.orderService = orderService;

        addClassName("order-view-card");
        setWidth("300px");
        setPadding(true);
        setSpacing(true);
        getStyle().set("border", "1px solid #ccc");
        getStyle().set("border-radius", "8px");
        getStyle().set("box-shadow", "2px 2px 8px rgba(0,0,0,0.1)");
        getStyle().set("margin", "10px");

        // Title
        H3 title = new H3("Order #" + order.getOrderId());

        // Customer name
        String customerName = order.getCustomer() != null
                ? order.getCustomer().getFirstName() + " " + order.getCustomer().getLastName()
                : "Unknown Customer";
        Paragraph customer = new Paragraph("Customer: " + customerName);

        // Customer address
        String customerAddress = order.getCustomer() != null
                ? order.getCustomer().getAddress()
                : "Unknown Address";
        Paragraph address = new Paragraph("Address: " + customerAddress);
        
        // Order date
        String formattedDate = order.getCreatedAt().format(formatter);
        Paragraph date = new Paragraph("Order date: " + formattedDate);

        // Dishes list
        List<Dish> dishes = order.getOrderedDishes();
        StringBuilder dishNames = new StringBuilder();
        for (Dish dish : dishes) {
            dishNames.append(dish.getDishName()).append(", ");
        }
        if (dishNames.length() > 2) {
            dishNames.setLength(dishNames.length() - 2);    // Remove last comma
        }
        Paragraph dishList = new Paragraph("Dishes: " + dishNames);

        double totalPrice = dishes.stream().mapToDouble(Dish::getPrice).sum();
        int totalCalories = dishes.stream().mapToInt(Dish::getCalories).sum();

        Paragraph totalPriceParagraph = new Paragraph("Total Price: €" + String.format("%.2f", totalPrice));
        Paragraph totalCaloriesParagraph = new Paragraph("Total Calories: " + totalCalories + " kcal");
        totalPriceParagraph.getStyle().set("font-weight", "bold");
        totalCaloriesParagraph.getStyle().set("font-weight", "bold");

        // ComboBox for editing status
        ComboBox<OrderStatus> statusComboBox = new ComboBox<>();
        statusComboBox.setLabel("Set Status");
        statusComboBox.setItems(OrderStatus.values());
        statusComboBox.setValue(order.getOrderStatus());
        statusComboBox.setWidth("180px");

        // Set up the badge initially
        updateStatusBadge(order.getOrderStatus());
        statusBadge.getStyle()
                .set("margin-left", "10px")
                .set("font-weight", "bold");
        statusBadge.setText(order.getOrderStatus().name());

        // Update badge and save order on status change
        statusComboBox.addValueChangeListener(event -> {
            OrderStatus newStatus = event.getValue();
            if (newStatus != null) {
                updateStatusBadge(newStatus);
                statusBadge.setText(newStatus.name());

                // save to database
                orderService.updateOrderStatus(order, newStatus);

                Notification.show("Order status updated!", 2000, Notification.Position.MIDDLE);
            }
        });

        add(title, statusBadge, customer, address, date, dishList, totalPriceParagraph, totalCaloriesParagraph, statusComboBox);
    }

    private void updateStatusBadge(OrderStatus status) {
        switch (status) {
            case PLACED -> statusBadge.getStyle().set("background-color", "#2196F3");
            case ACCEPTED -> statusBadge.getStyle().set("background-color", "#3F51B5");
            case IN_PREPARATION -> statusBadge.getStyle().set("background-color", "#FFC107").set("color", "black");
            case SHIPPED -> statusBadge.getStyle().set("background-color", "#009688");
            case ARRIVED -> statusBadge.getStyle().set("background-color", "#4CAF50");
            default -> statusBadge.getStyle().set("background-color", "#9E9E9E");
        }
        statusBadge.getStyle()
                .set("padding", "4px 8px")
                .set("border-radius", "8px")
                .set("color", status == OrderStatus.IN_PREPARATION ? "black" : "white");
    }
}
