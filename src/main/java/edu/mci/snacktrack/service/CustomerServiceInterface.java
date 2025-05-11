package edu.mci.snacktrack.service;


import edu.mci.snacktrack.model.Customer;

public interface CustomerServiceInterface {

    Customer createCustomer(String firstName,
                            String lastName,
                            String email,
                            String address);
}
