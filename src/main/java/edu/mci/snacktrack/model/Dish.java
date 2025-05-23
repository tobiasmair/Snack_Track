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
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> category;

    @Column(name="is_active", nullable = false)
    private boolean isActive;

    @ManyToOne
    @JoinColumn(name = "restaurantId")
    private Restaurant restaurant;

    public Dish(String dishName, String dishDescription, double price, int calories, int protein, List<String> category, Restaurant restaurant) {
        setDishName(dishName);
        setDishDescription(dishDescription);
        setPrice(price);
        setCalories(calories);
        setProtein(protein);
        setCategory(category);
        setRestaurant(restaurant);
        setActive(true);
    }
}
