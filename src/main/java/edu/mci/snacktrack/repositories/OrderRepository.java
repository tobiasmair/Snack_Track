package edu.mci.snacktrack.repositories;

import edu.mci.snacktrack.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
