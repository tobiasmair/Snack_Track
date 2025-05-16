package edu.mci.snacktrack.service;

import edu.mci.snacktrack.model.Cuisine;
import edu.mci.snacktrack.model.Restaurant;

import java.util.List;
import java.util.Optional;

public interface RestaurantServiceInterface {

    Restaurant createRestaurant(String restaurantName,
                                Cuisine cuisine,
                                String email,
                                String password,
                                String address,
                                String vatNr);

    List<Restaurant> getAllRestaurants();

    Optional<Restaurant> findById(Long id);
}
