package edu.mci.snacktrack.service;

import edu.mci.snacktrack.model.Dish;
import edu.mci.snacktrack.model.Order;
import edu.mci.snacktrack.model.Restaurant;

import java.util.List;

public interface DishServiceInterface {

    Dish createDish(String dishName,
                    String dishDescription,
                    double price,
                    int calories,
                    int protein,
                    List<String> category,
                    Restaurant restaurantId);

    List<Dish> findByRestaurant(Restaurant restaurant);

}
