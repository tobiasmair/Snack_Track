package edu.mci.snacktrack.model;

import jakarta.persistence.*;

import java.util.List;
import java.util.UUID;

@Entity
public class Order {
    @Id
    @GeneratedValue
    private UUID orderId;

    @ManyToMany
    private List<Dish> dishes;

    @ManyToOne
    private Customer customer;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

}

