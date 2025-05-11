package edu.mci.snacktrack.service.implementation;

import edu.mci.snacktrack.model.Restaurant;
import edu.mci.snacktrack.repositories.RestaurantRepository;
import edu.mci.snacktrack.service.RestaurantServiceInterface;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class RestaurantService implements RestaurantServiceInterface {

    private RestaurantRepository restaurantRepository;


    @Override
    public Restaurant createRestaurant(String restaurantName, String cuisine, String address, String email, String vatNr) {

        Restaurant newRestaurant = new Restaurant(restaurantName, cuisine, address, email, vatNr);
        restaurantRepository.save(newRestaurant);
        return newRestaurant;
    }
}
