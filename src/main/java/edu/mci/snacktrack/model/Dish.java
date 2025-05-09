package edu.mci.snacktrack.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import java.util.List;
import java.util.UUID;

@Entity
public class Dish {
    @Id
    @GeneratedValue
    private final UUID dishId;

    private String dishName;
    private String dishDescription;
    private double price;
    private Calories calories;
    private List<String> category;

    public Dish(String dishName, String description, float price, int caloriesInKcal){
        this.dishId = UUID.randomUUID();
        setDishName(dishName);
        setDishDescription(description);
        setPrice(price);
        this.calories = new Calories(caloriesInKcal);
    }

    // getter & setter
    public UUID getDishId() {
        return dishId;
    }

    public String getDishName() {
        return dishName;
    }

    public void setDishName(String dishName) {
        this.dishName = dishName;
    }

    public String getDishDescription() {
        return dishDescription;
    }

    public void setDishDescription(String dishDescription) {
        this.dishDescription = dishDescription;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getCaloriesInKcal(){
        return calories.getCaloriesInKcal();
    }

    public void setCaloriesInKcal(int caloriesInKcal){
        this.calories.setCaloriesInKcal(caloriesInKcal);
    }

    public int getCaloriesInKJoule(){
        return calories.getCaloriesInKJoule();
    }


}
