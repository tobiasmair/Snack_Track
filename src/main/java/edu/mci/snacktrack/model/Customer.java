package edu.mci.snacktrack.model;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;

import java.util.List;

@Entity
public class Customer extends User{
    private String firstName;
    private String lastName;
    private String email;
    private String address;

    @OneToMany(mappedBy = "customer")
    private List<Order> orderHistory;

    public Customer(String username,
                    String password,
                    String firstName,
                    String lastName,
                    String email,
                    String address){

        super(username, password);
        setFirstName(firstName);
        setLastName(lastName);
        setEmail(email);
        setAddress(address);


    }

    // getter & setter
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<Order> getOrderHistory() {
        return orderHistory;
    }



    // TODO: add function to append orderHistory
}
