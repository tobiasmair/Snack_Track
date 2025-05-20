package edu.mci.snacktrack.service.implementation;

import edu.mci.snacktrack.model.Cuisine;
import edu.mci.snacktrack.model.Restaurant;
import edu.mci.snacktrack.repositories.CustomerRepository;
import edu.mci.snacktrack.repositories.OrderRepository;
import edu.mci.snacktrack.repositories.RestaurantRepository;
import edu.mci.snacktrack.service.RestaurantServiceInterface;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@AllArgsConstructor
public class RestaurantService implements RestaurantServiceInterface {

    private RestaurantRepository restaurantRepository;
    private CustomerRepository customerRepository;
    private OrderRepository orderRepository;

    @Override
    public Restaurant createRestaurant(String restaurantName, Cuisine cuisine, String email, String password, String address, String vatNr) {

        if (customerRepository.findByEmail(email).isPresent() || restaurantRepository.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("A user with this email already exists.");
        }

        Restaurant newRestaurant = new Restaurant(restaurantName, cuisine, email, password, address, vatNr);
        restaurantRepository.save(newRestaurant);
        return newRestaurant;
    }

    @Override
    public List<Restaurant> getAllRestaurants() {
        return restaurantRepository.findAll();
    }

    @Override
    public Optional<Restaurant> findById(Long id) {
        return restaurantRepository.findById(id);
    }

    public Optional<Restaurant> findByIdWithMenu(Long id) {
        return restaurantRepository.findByIdWithMenu(id);
    }

    public Map<String, Number> getSalesStats(Long restaurantId, LocalDateTime from, LocalDateTime to) {
        Map<String, Number> result = orderRepository.getSalesStats(restaurantId, from, to);

        Number totalSales = result.getOrDefault("totalSales", 0);
        Number orderCount = result.getOrDefault("orderCount", 0);

        Map<String, Number> stats = new HashMap<>();
        stats.put("totalSales", totalSales);
        stats.put("orderCount", orderCount);

        return stats;
    }

}
