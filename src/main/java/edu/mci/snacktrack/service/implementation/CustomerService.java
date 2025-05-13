package edu.mci.snacktrack.service.implementation;

import edu.mci.snacktrack.model.Customer;
import edu.mci.snacktrack.repositories.CustomerRepository;
import edu.mci.snacktrack.repositories.RestaurantRepository;
import edu.mci.snacktrack.service.CustomerServiceInterface;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CustomerService implements CustomerServiceInterface {

        private CustomerRepository customerRepository;
        private RestaurantRepository restaurantRepository;

    @Override
    public Customer createCustomer(String firstName, String lastName, String email, String password, String address) {

        if (customerRepository.findByEmail(email).isPresent() || restaurantRepository.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("A user with this email already exists.");
        }

        Customer newCustomer = new Customer(firstName, lastName, email, password, address);
        customerRepository.save(newCustomer);

        return newCustomer;
    }
}
