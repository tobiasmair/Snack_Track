package edu.mci.snacktrack.mapper;

import edu.mci.snacktrack.dto.CustomerDTO;
import edu.mci.snacktrack.model.Customer;

public class CustomerMapper {

    public static CustomerDTO mapToCustomerDTO(Customer customer){
        return new CustomerDTO(
                customer.getCustomerId(),
                customer.getFirstName(),
                customer.getLastName(),
                customer.getEmail(),
                customer.getAddress(),
                customer.getOrderHistory()
        );
    }

    public static Customer mapToCustomer(CustomerDTO customerDTO){
        return new Customer(
                customerDTO.getCustomerId(),
                customerDTO.getFirstName(),
                customerDTO.getLastName(),
                customerDTO.getEmail(),
                customerDTO.getAddress(),
                customerDTO.getOrderHistory()
        );
    }
}
