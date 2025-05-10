package edu.mci.snacktrack.mapper;

import edu.mci.snacktrack.dto.DishDTO;
import edu.mci.snacktrack.model.Dish;

public class DishMapper {

    public static DishDTO mapToDishDTO(Dish dish){
        return new DishDTO(
                dish.getDishId(),
                dish.getDishName(),
                dish.getDishDescription(),
                dish.getPrice(),
                dish.getCalories(),
                dish.getProtein(),
                dish.getCategory(),
                dish.getRestaurant()
        );
    }

    public static Dish mapToDish(DishDTO dishDTO){
        return new Dish(
                dishDTO.getDishId(),
                dishDTO.getDishName(),
                dishDTO.getDishDescription(),
                dishDTO.getPrice(),
                dishDTO.getCalories(),
                dishDTO.getProtein(),
                dishDTO.getCategory(),
                dishDTO.getRestaurant()
        );
    }
}
