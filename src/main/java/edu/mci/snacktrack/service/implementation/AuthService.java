package edu.mci.snacktrack.service.implementation;

import edu.mci.snacktrack.model.Customer;
import edu.mci.snacktrack.model.Restaurant;
import edu.mci.snacktrack.repositories.CustomerRepository;
import edu.mci.snacktrack.repositories.RestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    private final CustomerRepository customerRepo;
    private final RestaurantRepository restaurantRepo;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthService(CustomerRepository customerRepo, RestaurantRepository restaurantRepo, PasswordEncoder passwordEncoder) {
        this.customerRepo = customerRepo;
        this.restaurantRepo = restaurantRepo;
        this.passwordEncoder = passwordEncoder;

    }

    public String authenticate(String email, String password) {
        // Check for empty or null input
        if (email == null || email.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            return "Email and password are required.";
        }

        Optional<Customer> customerOpt = customerRepo.findByEmail(email.trim());
        if (customerOpt.isPresent()) {
            Customer customer = customerOpt.get();
            if (!customer.isActive()) {
                return "This customer has been deleted or deactivated.";     // For debugging -> in production this would say "No user found with those credentials."
            }

            if (passwordEncoder.matches(password, customer.getPassword())) {
                return "customer";
            } else {
                return "Incorrect password for customer.";
            }
        }

        Optional<Restaurant> restaurantOpt = restaurantRepo.findByEmail(email.trim());
        if (restaurantOpt.isPresent()) {
            Restaurant restaurant = restaurantOpt.get();
            if (!restaurant.isActive()) {
                return "This restaurant has been deleted or deactivated.";  // For debugging -> in production this would say "No user found with those credentials."
            }

            if (passwordEncoder.matches(password, restaurant.getPassword())) {
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