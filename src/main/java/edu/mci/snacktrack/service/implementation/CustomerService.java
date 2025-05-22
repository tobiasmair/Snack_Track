package edu.mci.snacktrack.service.implementation;

import edu.mci.snacktrack.model.Customer;
import edu.mci.snacktrack.repositories.CustomerRepository;
import edu.mci.snacktrack.repositories.RestaurantRepository;
import edu.mci.snacktrack.service.CustomerServiceInterface;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

@Service
@AllArgsConstructor
public class CustomerService implements CustomerServiceInterface {

        private final CustomerRepository customerRepository;
        private final RestaurantRepository restaurantRepository;
        private final PasswordEncoder passwordEncoder;

    @Override
    public Customer createCustomer(String firstName, String lastName, String email, String password, String address) {
        if (customerRepository.findByEmail(email).isPresent() || restaurantRepository.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("A user with this email already exists.");
        }

        String hashedPassword = passwordEncoder.encode(password);

        Customer newCustomer = new Customer(firstName, lastName, email, hashedPassword, address);
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

        String hashedPassword = passwordEncoder.encode(newPassword);
        customer.setPassword(hashedPassword);
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

    public void softdeleteCustomer(Long customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new IllegalArgumentException("Customer not found"));

        customer.setActive(false);
        customer.setFirstName("");
        customer.setLastName("");
        customer.setEmail("");
        customer.setPassword("");
        customer.setAddress("");
        customerRepository.save(customer);
    }

}
