package edu.mci.snacktrack.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "customer")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long customerId;

    private String firstName;
    private String lastName;
    @Column(name= "email", nullable = false, unique = true)
    private String email;
    private String address;

    @OneToMany(mappedBy = "customer")
    private List<Order> orderHistory;

    public Customer(String firstName,
                    String lastName,
                    String email,
                    String address){
        setFirstName(firstName);
        setLastName(lastName);
        setEmail(email);
        setAddress(address);


    }

    // getter & setter
    public long getCustomerId(){
        return customerId;
    }

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
