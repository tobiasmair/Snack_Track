package edu.mci.snacktrack.service;

import edu.mci.snacktrack.model.Restaurant;

public interface RestaurantServiceInterface {

    Restaurant createRestaurant(String restaurantName,
                                String cuisine,
                                String email,
                                String password,
                                String address,
                                String vatNr);
}
