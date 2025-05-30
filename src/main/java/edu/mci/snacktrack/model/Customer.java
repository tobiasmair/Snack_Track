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
    @Column(name= "email", nullable = false, unique = true)
    private String email;
    private String password; // for now, store password unencrypted
    private String address;

    @Column(name="is_active", nullable = false)
    private boolean isActive;

    @OneToMany(mappedBy = "customer")
    private List<Order> orderHistory = new ArrayList<>();

    public Customer(String firstName, String lastName, String email, String password, String address) {
        setFirstName(firstName);
        setLastName(lastName);
        setEmail(email);
        setPassword(password);
        setAddress(address);
        setActive(true);
        this.orderHistory = new ArrayList<>();
    }

    public Long getId() {
        return customerId;
    }
}
