package edu.mci.snacktrack.service.implementation;

import edu.mci.snacktrack.model.Cuisine;
import edu.mci.snacktrack.model.Restaurant;
import edu.mci.snacktrack.repositories.CustomerRepository;
import edu.mci.snacktrack.repositories.OrderRepository;
import edu.mci.snacktrack.repositories.RestaurantRepository;
import edu.mci.snacktrack.service.RestaurantServiceInterface;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class RestaurantService implements RestaurantServiceInterface {

    private RestaurantRepository restaurantRepository;
    private CustomerRepository customerRepository;
    private OrderRepository orderRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Restaurant createRestaurant(String restaurantName, Cuisine cuisine, String email, String password, String address, String vatNr) {
        if (customerRepository.findByEmail(email).isPresent() || restaurantRepository.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("A user with this email already exists.");
        }

        String hashedPassword = passwordEncoder.encode(password);

        Restaurant newRestaurant = new Restaurant(restaurantName, cuisine, email, hashedPassword, address, vatNr);
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


    // Update/Edit Restaurant Profile
    public Restaurant updateRestaurantName(Long restaurantId, String newName) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new IllegalArgumentException("Restaurant not found"));
        restaurant.setRestaurantName(newName);
        return restaurantRepository.save(restaurant);
    }


    public Restaurant updateRestaurantCuisine(Long restaurantId, Cuisine newCuisine) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new IllegalArgumentException("Restaurant not found"));
        restaurant.setCuisine(newCuisine);
        return restaurantRepository.save(restaurant);
    }


    public Restaurant updateRestaurantEmail(Long restaurantId, String newEmail) {
        if (restaurantRepository.findByEmail(newEmail).isPresent() ||
                customerRepository.findByEmail(newEmail).isPresent()) {
            throw new IllegalArgumentException("Email already in use.");
        }
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new IllegalArgumentException("Restaurant not found"));
        restaurant.setEmail(newEmail);
        return restaurantRepository.save(restaurant);
    }


    public Restaurant updateRestaurantPassword(Long restaurantId, String newPassword) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new IllegalArgumentException("Restaurant not found"));

        String hashedPassword = passwordEncoder.encode(newPassword);  // Hash it!
        restaurant.setPassword(hashedPassword);
        return restaurantRepository.save(restaurant);
    }

    public Restaurant updateRestaurantAddress(Long restaurantId, String newAddress) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new IllegalArgumentException("Restaurant not found"));
        restaurant.setAddress(newAddress);
        return restaurantRepository.save(restaurant);
    }

    public Restaurant updateRestaurantVatNr(Long restaurantId, String newVatNr) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new IllegalArgumentException("Restaurant not found"));
        restaurant.setVatNr(newVatNr);
        return restaurantRepository.save(restaurant);
    }

    public Optional<Restaurant> getRestaurantByEmail(String email) {
        return restaurantRepository.findByEmail(email);
    }


    public void softDeleteRestaurant(Long restaurantId) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new IllegalArgumentException("Restaurant not found"));

        restaurant.setActive(false);
        restaurant.setRestaurantName("");
        restaurant.setEmail("");
        restaurant.setVatNr("");
        restaurant.setPassword("");
        restaurant.setAddress("");
        restaurantRepository.save(restaurant);
    }


    // Get Stats for Report Screen
    public Map<String, Number> getSalesStats(Long restaurantId, LocalDateTime from, LocalDateTime to) {
        Map<String, Number> result = orderRepository.getSalesStats(restaurantId, from, to);

        Number totalSales = result.getOrDefault("totalSales", 0);
        Number orderCount = result.getOrDefault("orderCount", 0);

        Map<String, Number> stats = new HashMap<>();
        stats.put("totalSales", totalSales);
        stats.put("orderCount", orderCount);

        return stats;
    }

    public Map<String, Integer> getSalesPerDish(Long restaurantId, LocalDateTime from, LocalDateTime to) {
        List<Object[]> results = orderRepository.getSalesPerDish(restaurantId, from, to);
        return results.stream()
                .collect(Collectors.toMap(
                        r -> (String) r[0],
                        r -> ((Long) r[1]).intValue()
                ));
    }

    public Map<String, Double> getSalesPerCustomer(Long restaurantId, LocalDateTime from, LocalDateTime to) {
        List<Object[]> results = orderRepository.getSalesPerCustomer(restaurantId, from, to);
        return results.stream()
                .collect(Collectors.toMap(
                        r -> (String) r[0],
                        r -> (Double) r[1]
                ));
    }

}
