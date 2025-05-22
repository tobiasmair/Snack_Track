package edu.mci.snacktrack.ui.customer;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import edu.mci.snacktrack.model.BasketSession;
import edu.mci.snacktrack.model.Dish;

import java.util.List;

public class MenuViewCard extends VerticalLayout {
    private boolean showButton;
    private Span quantityBadge;
    private Dish dish;

    public MenuViewCard(Dish dish) {
        this(dish, false, false);
    }

    public MenuViewCard(Dish dish, boolean showaddButton, boolean clearBasketButton) {

        addClassName("menu-view-card");
        setWidth("300px");
        setPadding(true);
        setSpacing(true);
        getStyle().set("border", "1px solid #ccc");
        getStyle().set("border-radius", "8px");
        getStyle().set("box-shadow", "2px 2px 8px rgba(0,0,0,0.1)");
        getStyle().set("margin", "10px");

        // Add Dish Image
        String imageFileName = dish.getDishName().replaceAll(" ", "") + ".png";
        String imagePath = "./images/" + imageFileName;
        Image dishImage = new Image(imagePath, "Image of " + dish.getDishName());
        dishImage.setWidth("100%");
        dishImage.getStyle().set("border-radius", "8px 8px 0 0");
        add(dishImage);

        // Dish name
        H3 name = new H3(dish.getDishName());
        name.getStyle().set("margin", "0 0 5px 0");

        Paragraph price = new Paragraph("Price: €" + dish.getPrice());
        Paragraph calories = new Paragraph("Calories: " + dish.getCalories());
        Paragraph description = new Paragraph(dish.getDishDescription());
        Paragraph categories = new Paragraph("Categories: " + String.join(", ", dish.getCategory()));

        add(name, price, calories, description, categories);

        if (showaddButton) {
            Button button = new Button("Add to basket");
            button.addClickListener(event -> {
                button.setEnabled(false);

                try {
                    BasketSession.addDish(dish);
                    Notification.show(dish.getDishName() + " added to basket!", 1000, Notification.Position.MIDDLE);
                } catch (IllegalArgumentException e) {
                    Notification.show(e.getMessage(), 3000, Notification.Position.MIDDLE);
                }

                // Enable button after timer
                UI ui = UI.getCurrent();
                new Thread(() -> {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ignored) {}
                    ui.access(() -> button.setEnabled(true));
                }).start();
            });
            add(button);
        }

        if (clearBasketButton) {

            // Quantity badge (hidden by default)
            quantityBadge = new Span();
            quantityBadge.getStyle().set("font-weight", "bold");
            quantityBadge.getStyle().set("background-color", "#cce5ff");
            quantityBadge.getStyle().set("border-radius", "12px");
            quantityBadge.getStyle().set("padding", "2px 8px");
            quantityBadge.setVisible(false);
            add(quantityBadge);

            Button clearBasket = new Button("Remove dish");
            clearBasket.addClickListener(event -> {
                BasketSession.removeDish(dish);
                UI.getCurrent().getPage().reload();
            });
            add(clearBasket);
        }
    }

    public void setQuantity(long qty) {
        if (qty > 1) {
            quantityBadge.setText("× " + qty);
            quantityBadge.setVisible(true);
        } else {
            quantityBadge.setVisible(false);
        }
    }

}
