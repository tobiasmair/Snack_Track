package edu.mci.snacktrack.service;

import edu.mci.snacktrack.model.Dish;

import java.util.List;

public interface DishServiceInterface {

    Dish createDish(String dishName,
                    String dishDescription,
                    double price,
                    int calories,
                    int protein,
                    List<String> category);
}
