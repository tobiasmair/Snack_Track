package edu.mci.snacktrack.ui.customer;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import edu.mci.snacktrack.model.BasketSession;
import edu.mci.snacktrack.model.Dish;
import org.apache.catalina.LifecycleState;

import java.util.List;

public class MenuViewCard extends VerticalLayout {
    private boolean showButton;

    public MenuViewCard(Dish dish) {
        this(dish, false);
    }

    public MenuViewCard(Dish dish, boolean showButton) {

        addClassName("menu-view-card");
        setWidth("300px");
        setPadding(true);
        setSpacing(true);
        getStyle().set("border", "1px solid #ccc");
        getStyle().set("border-radius", "8px");
        getStyle().set("box-shadow", "2px 2px 8px rgba(0,0,0,0.1)");
        getStyle().set("margin", "10px");

        H3 name = new H3(dish.getDishName());

        Paragraph price = new Paragraph("Price: €" + dish.getPrice());
        Paragraph calories = new Paragraph("Calories: " + dish.getCalories());
        Paragraph description = new Paragraph(dish.getDishDescription());

        add(name, price, calories, description);

        if (showButton) {
            Button button = new Button("Add to basket");
            button.addClickListener(event -> {
                button.setEnabled(false);

                BasketSession.addDish(dish);
                Notification.show(dish.getDishName() + " added to basket!", 3000, Notification.Position.MIDDLE);

                // Enable button after timer
                UI ui = UI.getCurrent();
                new Thread(() -> {
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException ignored) {}
                    ui.access(() -> button.setEnabled(true));
                }).start();
            });
            add(button);
        }
    }
}
