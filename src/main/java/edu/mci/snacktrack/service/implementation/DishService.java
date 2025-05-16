package edu.mci.snacktrack.service.implementation;

import edu.mci.snacktrack.model.Dish;
import edu.mci.snacktrack.model.Restaurant;
import edu.mci.snacktrack.repositories.DishRepository;
import edu.mci.snacktrack.service.DishServiceInterface;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class DishService implements DishServiceInterface {

    private DishRepository dishRepository;

    @Override
    public Dish createDish(String dishName, String dishDescription, double price, int calories, int protein, List<String> category, Restaurant restaurant) {
        Dish newDish = new Dish(dishName, dishDescription, price, calories, protein, category, restaurant);

        dishRepository.save(newDish);

        return newDish;
    }
}
