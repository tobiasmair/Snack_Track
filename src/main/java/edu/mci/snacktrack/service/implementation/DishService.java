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

    @Override
    public List<Dish> findByRestaurant(Restaurant restaurant) {
        return dishRepository.findByRestaurant(restaurant);
    }


    // Update/Edit Dish properties
    public Dish updateDishName(Long dishId, String newName) {
        Dish dish = dishRepository.findById(dishId)
                .orElseThrow(() -> new IllegalArgumentException("Dish not found"));
        dish.setDishName(newName);
        return dishRepository.save(dish);
    }

    public Dish updateDishDescription(Long dishId, String newDesc) {
        Dish dish = dishRepository.findById(dishId)
                .orElseThrow(() -> new IllegalArgumentException("Dish not found"));
        dish.setDishDescription(newDesc);
        return dishRepository.save(dish);
    }

    public Dish updateDishPrice(Long dishId, Double newPrice) {
        if (newPrice == null || newPrice < 0)
            throw new IllegalArgumentException("Price must be positive");
        Dish dish = dishRepository.findById(dishId)
                .orElseThrow(() -> new IllegalArgumentException("Dish not found"));
        dish.setPrice(newPrice);
        return dishRepository.save(dish);
    }

    public Dish updateDishCalories(Long dishId, Integer newCalories) {
        if (newCalories == null || newCalories < 0)
            throw new IllegalArgumentException("Calories must be positive");
        Dish dish = dishRepository.findById(dishId)
                .orElseThrow(() -> new IllegalArgumentException("Dish not found"));
        dish.setCalories(newCalories);
        return dishRepository.save(dish);
    }

    public Dish updateDishProtein(Long dishId, Integer newProtein) {
        if (newProtein == null || newProtein < 0)
            throw new IllegalArgumentException("Protein must be positive");
        Dish dish = dishRepository.findById(dishId)
                .orElseThrow(() -> new IllegalArgumentException("Dish not found"));
        dish.setProtein(newProtein);
        return dishRepository.save(dish);
    }


    public Dish deleteDish(Long dishId) {
        Dish dish = dishRepository.findById(dishId)
                .orElseThrow(() -> new IllegalArgumentException("Dish not found"));

        dish.setActive(false);
        dish.setDishName("");
        dish.setDishDescription("");
        dish.setPrice(0.0);
        dish.setCalories(0);
        dish.setProtein(0);

        return dishRepository.save(dish);
    }

}
