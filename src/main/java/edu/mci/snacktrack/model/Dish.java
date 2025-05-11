package edu.mci.snacktrack.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "dish")
public class Dish {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long dishId;

    private String dishName;
    private String dishDescription;
    private double price;
    private int calories;
    private int protein;
    @ElementCollection  //TODO: maybe remove
    private List<String> category;

    @ManyToOne
    @JoinColumn(name = "restaurantId")
    private Restaurant restaurant;

    public Dish(String dishName, String dishDescription, double price, int calories, int protein, List<String> category) {
        this.dishName = dishName;
        this.dishDescription =dishDescription;
        this.price = price;
        this.calories = calories;
        this.protein = protein;
        this.category = category;
        this.restaurant = null; // TODO this is null for testing -> later implement so that this is the restaurant account that creates the dish
    }
}
