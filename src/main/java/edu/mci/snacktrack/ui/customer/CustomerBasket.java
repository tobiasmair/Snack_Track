package edu.mci.snacktrack.ui.customer;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import edu.mci.snacktrack.model.BasketSession;
import edu.mci.snacktrack.model.Dish;

import java.util.List;

@Route(value = "customer-basket", layout = CustomerLayout.class)
@PageTitle("Customer Basket")
public class CustomerBasket extends VerticalLayout implements BeforeEnterObserver {

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        String role = (String) VaadinSession.getCurrent().getAttribute("userRole");
        if (!"customer".equals(role)) {
            event.forwardTo("");
        }
    }

    public CustomerBasket() {
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        H2 title = new H2("Your Basket");
        title.getStyle().set("margin-bottom", "20px");
        add(title);

        FlexLayout dishLayout = new FlexLayout();
        dishLayout.getStyle().set("flex-wrap", "wrap");
        dishLayout.getStyle().set("gap", "20px");
        dishLayout.setWidthFull();

        List<Dish> basketDishes = BasketSession.getBasket();

        if (basketDishes.isEmpty()) {
            add(new H2("Your basket is empty"));
        } else {
            basketDishes.forEach(dish -> {
                MenuViewCard dishCard = new MenuViewCard(dish, false);
                dishLayout.add(dishCard);
            });
            add(dishLayout);
        }

        Button checkoutButton = new Button("Checkout");
        checkoutButton.getStyle().set("margin-bottom", "20px");
        add(checkoutButton);

    }

}
