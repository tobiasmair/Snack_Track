package edu.mci.snacktrack.service.implementation;

import edu.mci.snacktrack.model.Customer;
import edu.mci.snacktrack.repositories.CustomerRepository;
import edu.mci.snacktrack.service.CustomerServiceInterface;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CustomerService implements CustomerServiceInterface {

        private CustomerRepository customerRepository;

    @Override
    public Customer createCustomer(String firstName, String lastName, String email, String address) {
        Customer newCustomer = new Customer(firstName, lastName, email, address);
        customerRepository.save(newCustomer);

        return newCustomer;
    }
}
