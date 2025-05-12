package edu.mci.snacktrack.repositories;

import edu.mci.snacktrack.model.Customer;
import edu.mci.snacktrack.model.Dish;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DishRepository extends JpaRepository<Dish, Long > {
}
