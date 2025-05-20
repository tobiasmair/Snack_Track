package edu.mci.snacktrack.model;

import com.vaadin.flow.server.VaadinSession;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class BasketSession {

    private static final String BASKET_KEY = "customer-basket";
    private static final String RESTAURANT_KEY = "customer-basket-restaurant";

    public static List<Dish> getBasket() {
        // Singleton Pattern
        List<Dish> basket = (List<Dish>) VaadinSession.getCurrent().getAttribute(BASKET_KEY);
        if (basket == null) {
            basket = new ArrayList<Dish>();
            VaadinSession.getCurrent().setAttribute(BASKET_KEY, basket);
        }
        return basket;
    }

    // Get the Restaurant from dish
    public static Restaurant getRestaurant() {
        return (Restaurant) VaadinSession.getCurrent().getAttribute(RESTAURANT_KEY);
    }

    // Add dish to basket
    public static void addDish(Dish dish) {
        List<Dish> basket = getBasket();

        Restaurant currentRestaurant = getRestaurant();

        if (basket.isEmpty()) {
            // Set restaurant for the dish
            VaadinSession.getCurrent().setAttribute(RESTAURANT_KEY, dish.getRestaurant());
        } else if (!dish.getRestaurant().getRestaurantId().equals(currentRestaurant.getRestaurantId())) {
            // If basket from another restaurant throw exception
            throw new IllegalArgumentException("Basket already contains dishes from restaurant: " + currentRestaurant.getRestaurantName());
        }
        basket.add(dish);
        VaadinSession.getCurrent().setAttribute(BASKET_KEY, basket);
    }

    // Remove dish
    public static void removeDish(Dish dish) {
        List<Dish> basket = getBasket();
        basket.remove(dish);
        VaadinSession.getCurrent().setAttribute(BASKET_KEY, basket);
    }

    // Clear basket
    public static void clearBasket() {
        VaadinSession.getCurrent().setAttribute(BASKET_KEY, new ArrayList<Dish>());
        VaadinSession.getCurrent().setAttribute(RESTAURANT_KEY, null);
    }

    public static Map<Dish, Long> getDishCounts() {
        List<Dish> basket = getBasket();
        return basket.stream()
                .collect(Collectors.groupingBy(
                        dish -> dish,
                        Collectors.counting()
                ));
    }

}
