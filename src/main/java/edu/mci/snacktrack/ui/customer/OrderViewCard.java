package edu.mci.snacktrack.ui.customer;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import edu.mci.snacktrack.model.Dish;
import edu.mci.snacktrack.model.Order;
import edu.mci.snacktrack.model.OrderStatus;
import edu.mci.snacktrack.service.implementation.OrderService;

import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class OrderViewCard extends VerticalLayout {

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public OrderViewCard(Order order, OrderService orderService) {

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

        // Status with colored badge
        Span statusBadge = createStatusBadge(order.getOrderStatus());

        // Restaurant name
        Paragraph restaurant = new Paragraph("Restaurant: " + order.getRestaurant().getRestaurantName());

        //Paragraph dishes = new Paragraph("Dishes:");
        //order.getOrderedDishes().forEach(dish -> {dishes.add(dish.getDishName() + ", ");}); // Get the name of every Dish in the order

        // Formatted order date
        String formattedDate = order.getCreatedAt().format(formatter);
        Paragraph date = new Paragraph("Order date: " + formattedDate);

        // List ordered dishes
        List<Dish> dishes = order.getOrderedDishes();

        StringBuilder dishNames = new StringBuilder();
        for (Dish dish : dishes) {
            dishNames.append(dish.getDishName()).append(", ");
        }
        if (dishNames.length() > 2) {
            dishNames.setLength(dishNames.length() - 2);    // Delete last comma
        }
        Paragraph dishList = new Paragraph("Dishes: " + dishNames);

        Paragraph totalPriceParagraph = new Paragraph("Total Price: €" + String.format("%.2f", order.getTotalPrice()));
        Paragraph totalCaloriesParagraph = new Paragraph("Total Calories: " + order.getTotalCalories() + " kcal");

        totalPriceParagraph.getStyle().set("font-weight", "bold");
        totalCaloriesParagraph.getStyle().set("font-weight", "bold");

        add(title, statusBadge, restaurant, date, dishList, totalPriceParagraph, totalCaloriesParagraph);

        if (order.getOrderStatus() == OrderStatus.PLACED) {
            Button deleteButton = new Button("Delete Order");
            deleteButton.addClickListener(event -> {
                boolean deleted = orderService.deleteOrder(order);
                if (deleted) {
                    Notification show = Notification.show("Order deleted!", 3000, Notification.Position.MIDDLE);
                } else {
                    Notification.show("Order could not be deleted!", 3000, Notification.Position.MIDDLE);
                }
                UI.getCurrent().getPage().reload();
            });
            deleteButton.getStyle().set("margin-top", "10px").set("background-color", "#f44336").set("color", "white");
            add(deleteButton);
        }

    }

    private Span createStatusBadge(OrderStatus status) {
        Span badge = new Span(status.name());
        badge.getStyle()
                .set("padding", "4px 8px")
                .set("border-radius", "8px")
                .set("color", "white")
                .set("font-weight", "bold");

        switch (status) {
            case PLACED -> badge.getStyle().set("background-color", "#2196F3");        // Blau
            case ACCEPTED -> badge.getStyle().set("background-color", "#3F51B5");      // Indigo
            case IN_PREPARATION -> badge.getStyle().set("background-color", "#FFC107"); // Gelb
            case SHIPPED -> badge.getStyle().set("background-color", "#009688");       // Türkis
            case ARRIVED -> badge.getStyle().set("background-color", "#4CAF50");       // Grün
            default -> badge.getStyle().set("background-color", "#9E9E9E");            // Grau
        }
        return badge;
    }

}
