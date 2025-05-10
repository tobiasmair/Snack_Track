package edu.mci.snacktrack.mapper;

import edu.mci.snacktrack.dto.OrderDTO;
import edu.mci.snacktrack.model.Order;

public class OrderMapper {

    public static OrderDTO mapToOrderDTO(Order order){
        return new OrderDTO(
                order.getOrderId(),
                order.getOrderedDishes(),
                order.getCustomer(),
                order.getRestaurant(),
                order.getOrderStatus(),
                order.getCreatedAt()
        );
    }

    public static Order mapToOrder(OrderDTO orderDTO){
        return new Order(
                orderDTO.getOrderId(),
                orderDTO.getOrderedDishes(),
                orderDTO.getCustomer(),
                orderDTO.getRestaurant(),
                orderDTO.getOrderStatus(),
                orderDTO.getCreatedAt()
        );
    }
}
