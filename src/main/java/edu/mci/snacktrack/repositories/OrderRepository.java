package edu.mci.snacktrack.repositories;

import edu.mci.snacktrack.model.Customer;
import edu.mci.snacktrack.model.Order;
import edu.mci.snacktrack.model.OrderStatus;
import edu.mci.snacktrack.model.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByCustomer(Customer customer);

    @Query("SELECT o FROM Order o JOIN FETCH o.orderedDishes WHERE o.customer = :customer")
    List<Order> findByCustomerWithDishes(@Param("customer") Customer customer);

    @Query("SELECT DISTINCT o FROM Order o LEFT JOIN FETCH o.orderedDishes WHERE o.restaurant = :restaurant AND o.orderStatus IN :statuses")
    List<Order> findByRestaurantAndOrderStatusInWithDishes(
            @Param("restaurant") Restaurant restaurant,
            @Param("statuses") List<OrderStatus> statuses
    );

    @Query("SELECT new map(COALESCE(SUM(o.totalPrice), 0) as totalSales, COUNT(o) as orderCount) " +
            "FROM Order o WHERE o.restaurant.restaurantId = :restaurantId " +
            "AND o.createdAt BETWEEN :from AND :to")
    Map<String, Number> getSalesStats(Long restaurantId, LocalDateTime from, LocalDateTime to);
}
