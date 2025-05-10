package edu.mci.snacktrack.dto;

import edu.mci.snacktrack.model.Customer;
import edu.mci.snacktrack.model.Dish;
import edu.mci.snacktrack.model.OrderStatus;
import edu.mci.snacktrack.model.Restaurant;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {
    private Long orderId;
    private List<Dish> orderedDishes;
    private Customer customer;
    private Restaurant restaurant;
    private OrderStatus orderStatus;
    private LocalDateTime createdAt;
}
