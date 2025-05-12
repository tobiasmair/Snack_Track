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
@Table(name = "restaurant")
public class Restaurant{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long restaurantId;
    private String restaurantName;
    private String cuisine;
    @Column(nullable = false, unique = false)    //TODO set unique to 'true' after debugging
    private String email;
    private String password; // for now, store password unencrypted
    private String address;
    private String vatNr;

    @OneToMany(mappedBy = "restaurant")
    private List<Dish> menu  = new ArrayList<>();

    public Restaurant(String restaurantName, String cuisine, String email, String password, String address, String vatNr) {
        setRestaurantName(restaurantName);
        setCuisine(cuisine);
        setEmail(email);
        setPassword(password);
        setAddress(address);
        setVatNr(vatNr);
        this.menu = new ArrayList<>();
    }
}
