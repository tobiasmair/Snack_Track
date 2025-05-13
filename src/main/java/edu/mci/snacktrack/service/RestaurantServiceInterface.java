package edu.mci.snacktrack.service;

import edu.mci.snacktrack.model.Cuisine;
import edu.mci.snacktrack.model.Restaurant;

public interface RestaurantServiceInterface {

    Restaurant createRestaurant(String restaurantName,
                                Cuisine cuisine,
                                String email,
                                String password,
                                String address,
                                String vatNr);
}
