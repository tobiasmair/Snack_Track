package edu.mci.snacktrack.model;

import com.vaadin.flow.server.VaadinSession;

import java.util.ArrayList;
import java.util.List;

public class BasketSession {

    private static final String BASKET_KEY = "customer-basket";

    public static List<Dish> getBasket() {
        // Singleton Pattern
        List<Dish> basket = (List<Dish>) VaadinSession.getCurrent().getAttribute(BASKET_KEY);
        if (basket == null) {
            basket = new ArrayList<Dish>();
            VaadinSession.getCurrent().setAttribute(BASKET_KEY, basket);
        }
        return basket;
    }

    // Add dish to basket
    public static void addDish(Dish dish) {
        getBasket().add(dish);
    }

    // Remove dish
    public static void removeDish(Dish dish) {
        getBasket().remove(dish);
    }

    // Clear basket
    public static void clearBasket() {
        getBasket().clear();
    }

}
