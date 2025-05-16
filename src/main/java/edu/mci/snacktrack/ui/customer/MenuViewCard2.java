package edu.mci.snacktrack.ui.customer;

import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.button.Button;
import edu.mci.snacktrack.model.BasketSession;
import edu.mci.snacktrack.model.Dish;

import java.util.List;

/**
 * MenuViewCard2: separate card implementation using BasketSession2
 */
public class MenuViewCard2 extends VerticalLayout {
    private final Dish dish;
    private final H3 name;
    private final Span quantityBadge;
    private final Button addButton;
    private final Button removeButton;

    public MenuViewCard2(Dish dish, boolean showButtons) {
        this.dish = dish;
        setWidth("200px");
        getStyle().set("border", "1px solid #007bff");
        getStyle().set("border-radius", "8px");
        getStyle().set("padding", "10px");
        getStyle().set("box-shadow", "2px 2px 5px rgba(0,123,255,0.2)");

        // Dish name
        name = new H3(dish.getDishName());
        name.getStyle().set("margin", "0 0 5px 0");
        add(name);

        // Quantity badge (hidden by default)
        quantityBadge = new Span();
        quantityBadge.getStyle().set("font-weight", "bold");
        quantityBadge.getStyle().set("background-color", "#cce5ff");
        quantityBadge.getStyle().set("border-radius", "12px");
        quantityBadge.getStyle().set("padding", "2px 8px");
        quantityBadge.setVisible(false);
        add(quantityBadge);

        // Price display
        Span price = new Span(String.format("€ %.2f", dish.getPrice()));
        price.getStyle().set("color", "#555");
        price.getStyle().set("font-size", "14px");
        add(price);

        // Add/remove buttons
        addButton = new Button("Add");
        removeButton = new Button("Remove");
        if (showButtons) {
            add(removeButton, addButton);
        }

        // Button logic using BasketSession2
        addButton.addClickListener(event -> {
            BasketSession.addDish(dish);
            updateQuantity(BasketSession.getBasket());
        });
        removeButton.addClickListener(event -> {
            BasketSession.removeDish(dish);
            updateQuantity(BasketSession.getBasket());
        });

        // Initialize badge based on current basket
        updateQuantity(BasketSession.getBasket());
    }

    private void updateQuantity(List<Dish> list) {
        int current = (int) list.stream().filter(d -> d.equals(dish)).count();
        setQuantity(current);
    }

    /**
     * Updates the badge text and visibility based on the quantity
     */
    public void setQuantity(int qty) {
        if (qty > 1) {
            quantityBadge.setText("× " + qty);
            quantityBadge.setVisible(true);
        } else {
            quantityBadge.setVisible(false);
        }
    }
}
