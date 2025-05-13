package edu.mci.snacktrack.service.implementation;

import edu.mci.snacktrack.model.Cuisine;
import edu.mci.snacktrack.model.Restaurant;
import edu.mci.snacktrack.repositories.CustomerRepository;
import edu.mci.snacktrack.repositories.RestaurantRepository;
import edu.mci.snacktrack.service.RestaurantServiceInterface;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class RestaurantService implements RestaurantServiceInterface {

    private RestaurantRepository restaurantRepository;
    private CustomerRepository customerRepository;


    @Override
    public Restaurant createRestaurant(String restaurantName, Cuisine cuisine, String email, String password, String address, String vatNr) {

        if (customerRepository.findByEmail(email).isPresent() || restaurantRepository.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("A user with this email already exists.");
        }

        Restaurant newRestaurant = new Restaurant(restaurantName, cuisine, email, password, address, vatNr);
        restaurantRepository.save(newRestaurant);
        return newRestaurant;
    }
}
