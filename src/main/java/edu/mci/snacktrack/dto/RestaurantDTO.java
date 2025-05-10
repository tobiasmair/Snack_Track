package edu.mci.snacktrack.dto;

import edu.mci.snacktrack.model.Dish;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantDTO {

    private Long restaurantId;
    private String restaurantName;
    private String cuisine;
    private String address;
    private String email;
    private String vatNr;
    private List<Dish> menu;
}
