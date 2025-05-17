package edu.mci.snacktrack.service.implementation;

import edu.mci.snacktrack.model.*;
import edu.mci.snacktrack.repositories.CustomerRepository;
import edu.mci.snacktrack.repositories.OrderRepository;
import edu.mci.snacktrack.service.OrderServiceInterface;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class OrderService implements OrderServiceInterface {

    private final CustomerRepository customerRepository;
    private OrderRepository orderRepository;

    @Override
    public Order createOrder(List<Dish> dishes, Customer customer, Restaurant restaurant) {
        Order newOrder = new Order();

        newOrder.setOrderedDishes(dishes);
        newOrder.setCustomer(customer);
        newOrder.setRestaurant(restaurant);
        newOrder.setOrderStatus(OrderStatus.PLACED);

        return orderRepository.save(newOrder);
    }

    public List<Order> getOrderbyCustomer(Customer customer) {
        return orderRepository.findByCustomerWithDishes(customer);
    };
}
