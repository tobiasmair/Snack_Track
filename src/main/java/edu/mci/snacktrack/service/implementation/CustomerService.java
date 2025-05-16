package edu.mci.snacktrack.service.implementation;

import edu.mci.snacktrack.model.Customer;
import edu.mci.snacktrack.repositories.CustomerRepository;
import edu.mci.snacktrack.repositories.RestaurantRepository;
import edu.mci.snacktrack.service.CustomerServiceInterface;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class CustomerService implements CustomerServiceInterface {

        private final CustomerRepository customerRepository;
        private final RestaurantRepository restaurantRepository;

    @Override
    public Customer createCustomer(String firstName, String lastName, String email, String password, String address) {

        if (customerRepository.findByEmail(email).isPresent() || restaurantRepository.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("A user with this email already exists.");
        }

        Customer newCustomer = new Customer(firstName, lastName, email, password, address);
        customerRepository.save(newCustomer);

        return newCustomer;
    }

    // Update/Edit Customer Profile
    public Customer updateCustomerFirstName(Long customerId, String newFirstName) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new IllegalArgumentException("Customer not found"));
        customer.setFirstName(newFirstName);
        return customerRepository.save(customer);
    }

    public Customer updateCustomerLastName(Long customerId, String newLastName) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new IllegalArgumentException("Customer not found"));
        customer.setLastName(newLastName);
        return customerRepository.save(customer);
    }

    public Customer updateCustomerEmail(Long customerId, String newEmail) {
        if (customerRepository.findByEmail(newEmail).isPresent() ||
                restaurantRepository.findByEmail(newEmail).isPresent()) {
            throw new IllegalArgumentException("Email already in use.");
        }

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new IllegalArgumentException("Customer not found"));
        customer.setEmail(newEmail);
        return customerRepository.save(customer);
    }

    public Customer updateCustomerPassword(Long customerId, String newPassword) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new IllegalArgumentException("Customer not found"));
        customer.setPassword(newPassword); // Hashing sollte man noch einbauen
        return customerRepository.save(customer);
    }

    public Customer updateCustomerAddress(Long customerId, String newAddress) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new IllegalArgumentException("Customer not found"));
        customer.setAddress(newAddress);
        return customerRepository.save(customer);
    }

    public Optional<Customer> getCustomerByEmail(String email) {
        return customerRepository.findByEmail(email);
    }

}
