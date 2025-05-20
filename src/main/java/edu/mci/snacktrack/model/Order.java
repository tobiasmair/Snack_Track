package edu.mci.snacktrack.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;

    @ManyToMany
    @JoinTable(
            name = "ordered_dishes",
            joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "dish_id")
    )
    private List<Dish> orderedDishes = new ArrayList<>();

    @ManyToOne(optional = false)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne(optional = false)
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    private LocalDateTime createdAt;

    @Column(name = "total_price")
    private double totalPrice;

    @Column(name = "total_calories")
    private int totalCalories;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    public Order(){
        this.orderedDishes = new ArrayList<>();
        setOrderStatus(OrderStatus.PLACED);
    }

    public void calculateTotals() {
        this.totalPrice = orderedDishes.stream()
                .mapToDouble(Dish::getPrice)
                .sum();

        this.totalCalories = orderedDishes.stream()
                .mapToInt(Dish::getCalories)
                .sum();
    }
}

