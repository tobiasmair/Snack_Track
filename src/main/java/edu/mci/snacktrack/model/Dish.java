package edu.mci.snacktrack.model;

import jakarta.persistence.*;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "dish")
public class Dish {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long dishId;

    private String dishName;
    private String dishDescription;
    private double price;
    private int calories;
    private int protein;
    private List<String> category;

    @ManyToOne
    @JoinColumn(name = "restaurantId")
    private Restaurant restaurant;

    public Dish(String dishName, String description, float price, int caloriesInKcal, int protein){
        setDishName(dishName);
        setDishDescription(description);
        setPrice(price);
        setCaloriesInKcal(caloriesInKcal);
        setProtein(protein);
    }

    // getter & setter
    public long getDishId() {
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
        return calories;
    }

    public void setCaloriesInKcal(int caloriesInKcal){
        this.calories = caloriesInKcal;
    }

    public int getProtein() {
        return protein;
    }

    public void setProtein(int protein) {
        this.protein = protein;
    }

    public List<String> getCategory() {
        return category;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }
}
