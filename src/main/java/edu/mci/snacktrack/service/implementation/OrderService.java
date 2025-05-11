package edu.mci.snacktrack.service.implementation;

import edu.mci.snacktrack.model.Order;
import edu.mci.snacktrack.repositories.OrderRepository;
import edu.mci.snacktrack.service.OrderServiceInterface;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class OrderService implements OrderServiceInterface {

    private OrderRepository orderRepository;

    @Override
    public Order createOrder() {
        Order newOrder = new Order();
        orderRepository.save(newOrder);
        return newOrder;
    }
}
