package edu.mci.snacktrack.mapper;

import edu.mci.snacktrack.dto.RestaurantDTO;
import edu.mci.snacktrack.model.Restaurant;

public class RestaurantMapper {

    public static RestaurantDTO mapToRestaurantDTO(Restaurant restaurant){
        return new RestaurantDTO(
                restaurant.getRestaurantId(),
                restaurant.getRestaurantName(),
                restaurant.getCuisine(),
                restaurant.getAddress(),
                restaurant.getEmail(),
                restaurant.getVatNr(),
                restaurant.getMenu()
        );
    }

    public static Restaurant mapToRestaurant(RestaurantDTO restaurantDTO){
        return new Restaurant(
                restaurantDTO.getRestaurantId(),
                restaurantDTO.getRestaurantName(),
                restaurantDTO.getCuisine(),
                restaurantDTO.getAddress(),
                restaurantDTO.getEmail(),
                restaurantDTO.getVatNr(),
                restaurantDTO.getMenu()
        );
    }
}
