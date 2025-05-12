package edu.mci.snacktrack.service.implementation;

import edu.mci.snacktrack.model.Customer;
import edu.mci.snacktrack.model.Restaurant;
import edu.mci.snacktrack.repositories.CustomerRepository;
import edu.mci.snacktrack.repositories.RestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    private final CustomerRepository customerRepo;
    private final RestaurantRepository restaurantRepo;

    @Autowired
    public AuthService(CustomerRepository customerRepo, RestaurantRepository restaurantRepo) {
        this.customerRepo = customerRepo;
        this.restaurantRepo = restaurantRepo;
    }

    public String authenticate(String email, String password) {
        // Check for empty or null input
        if (email == null || email.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            return "Email and password are required.";
        }

        Optional<Customer> customerOpt = customerRepo.findByEmail(email.trim());
        if (customerOpt.isPresent()) {
            Customer customer = customerOpt.get();
            if (password.equals(customer.getPassword())) {
                return "customer";
            } else {
                return "Incorrect password for customer.";
            }
        }

        Optional<Restaurant> restaurantOpt = restaurantRepo.findByEmail(email.trim());
        if (restaurantOpt.isPresent()) {
            Restaurant restaurant = restaurantOpt.get();
            if (password.equals(restaurant.getPassword())) {
                return "restaurant";
            } else {
                return "Incorrect password for restaurant.";
            }
        }

        return "No user found with those credentials.";
    }


    public Customer getCustomerByEmail(String email) {
        return customerRepo.findByEmail(email).orElse(null);
    }

    public Restaurant getRestaurantByEmail(String email) {
        return restaurantRepo.findByEmail(email).orElse(null);
    }
}