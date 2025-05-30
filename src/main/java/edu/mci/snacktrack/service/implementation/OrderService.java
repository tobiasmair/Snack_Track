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

        // Calculate Price and Calories
        newOrder.calculateTotals();

        return orderRepository.save(newOrder);
    }

    public List<Order> getOrderbyCustomer(Customer customer) {
        return orderRepository.findByCustomerWithDishes(customer);
    };

    public List<Order> getOpenOrdersByRestaurant(Restaurant restaurant) {
        return orderRepository.findByRestaurantAndOrderStatusInWithDishes(
                restaurant,
                List.of(OrderStatus.PLACED, OrderStatus.ACCEPTED, OrderStatus.IN_PREPARATION, OrderStatus.SHIPPED)
        );
    }

    public void updateOrderStatus(Order order, OrderStatus newStatus) {
        order.setOrderStatus(newStatus);
        orderRepository.save(order);
    }

    public List<Order> getPastOrdersByRestaurant(Restaurant restaurant) {
        return orderRepository.findByRestaurantAndOrderStatusInWithDishes(
                restaurant,
                List.of(OrderStatus.ARRIVED)
        );
    }

    public boolean deleteOrder(Order order) {
        // Delete order before Accepted from restaurant
        if (order.getOrderStatus() == OrderStatus.PLACED) {
            orderRepository.delete(order);
            return true;
        }

        return false;
    }


}
