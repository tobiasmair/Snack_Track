package edu.mci.snacktrack.repositories;

import edu.mci.snacktrack.model.Customer;
import edu.mci.snacktrack.model.Dish;
import edu.mci.snacktrack.model.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DishRepository extends JpaRepository<Dish, Long > {
    List<Dish> findByRestaurant(Restaurant restaurant);
}
