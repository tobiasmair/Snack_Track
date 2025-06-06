package edu.mci.snacktrack.service;


import edu.mci.snacktrack.model.*;
import org.aspectj.weaver.ast.Or;

import java.util.List;

public interface OrderServiceInterface {

    Order createOrder(List<Dish> dishes, Customer customer, Restaurant restaurant);

    List<Order> getOrderbyCustomer(Customer customer);

}
