package edu.mci.snacktrack.model;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;

import java.util.List;

@Entity
public class Restaurant extends User{
    private String restaurantName;
    private String address;
    private String email;
    private String vatNr;

    @OneToMany(mappedBy = "restaurant")
    private List<Dish> menu;

    public Restaurant(String username,
                      String password,
                      String restaurantName,
                      String address,
                      String email,
                      String vatNr){

        super(username, password);
        setRestaurantName(restaurantName);
        setAddress(address);
        setEmail(email);
        setVatNr(vatNr);
    }

    // getter & setter
    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getVatNr() {
        return vatNr;
    }

    public void setVatNr(String vatNr) {
        this.vatNr = vatNr;
    }

    // TODO: add function to append menu


}
