package edu.mci.snacktrack.service;

import edu.mci.snacktrack.dto.CustomerDTO;
import edu.mci.snacktrack.mapper.CustomerMapper;
import edu.mci.snacktrack.model.Customer;
import edu.mci.snacktrack.repositories.CustomerRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CustomerService implements CustomerServiceInterface{

    private CustomerRepository customerRepository;

    @Override
    public CustomerDTO createCustomer(CustomerDTO customerDTO) {

        Customer customer = CustomerMapper.mapToCustomer(customerDTO);
        Customer savedCustomer = customerRepository.save(customer);

        return CustomerMapper.mapToCustomerDTO(savedCustomer);
    }
}
