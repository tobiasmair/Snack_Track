package edu.mci.snacktrack.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "restaurant")
public class Restaurant{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long restaurantId;
    private String restaurantName;
    private String cuisine;
    private String address;
    private String email;
    private String vatNr;

    @OneToMany(mappedBy = "restaurant")
    private List<Dish> menu;

    public Restaurant(
                      String restaurantName,
                      String cuisine,
                      String address,
                      String email,
                      String vatNr){
        setRestaurantName(restaurantName);
        setCuisine(cuisine);
        setAddress(address);
        setEmail(email);
        setVatNr(vatNr);
    }

    // getter & setter
    public long getRestaurantId(){
        return restaurantId;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public String getCuisine(){
        return cuisine;
    }

    public void setCuisine(String cuisine){
        this.cuisine = cuisine;
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

    public List<Dish> getMenu() {
        return menu;
    }
}
