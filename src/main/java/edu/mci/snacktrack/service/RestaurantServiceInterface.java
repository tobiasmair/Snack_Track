package edu.mci.snacktrack.service;

import edu.mci.snacktrack.model.Restaurant;

public interface RestaurantServiceInterface {

    Restaurant createRestaurant(String restaurantName,
                                String cuisine,
                                String address,
                                String email,
                                String vatNr);
}
