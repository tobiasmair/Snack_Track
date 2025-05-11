package edu.mci.snacktrack.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "customer")
public class Customer {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long customerId;

    private String firstName;
    private String lastName;
    @Column(name= "email", nullable = false, unique = false)    //TODO set unique to 'true' after debugging
    private String email;
    private String address;

    @OneToMany(mappedBy = "customer")
    private List<Order> orderHistory = new ArrayList<>();

    public Customer(String firstName, String lastName, String email, String address) {
        this.setFirstName(firstName);
        this.setLastName(lastName);
        this.setEmail(email);
        this.setAddress(address);
        this.orderHistory = new ArrayList<>();
    }
}
