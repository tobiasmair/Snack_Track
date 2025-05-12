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
    public Restaurant createRestaurant(String restaurantName, String cuisine, String email, String password, String address,  String vatNr) {

        Restaurant newRestaurant = new Restaurant(restaurantName, cuisine, email, password, address, vatNr);
        restaurantRepository.save(newRestaurant);
        return newRestaurant;
    }
}
