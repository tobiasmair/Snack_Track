package edu.mci.snacktrack.repositories;

import edu.mci.snacktrack.model.Customer;
import edu.mci.snacktrack.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByCustomer(Customer customer);

    @Query("SELECT o FROM Order o JOIN FETCH o.orderedDishes WHERE o.customer = :customer")
    List<Order> findByCustomerWithDishes(@Param("customer") Customer customer);
}
