package edu.mci.snacktrack.dto;

import edu.mci.snacktrack.model.Restaurant;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DishDTO {

    private Long dishId;
    private String dishName;
    private String dishDescription;
    private double price;
    private int calories;
    private int protein;
    private List<String> category;
    private Restaurant restaurant;
}
