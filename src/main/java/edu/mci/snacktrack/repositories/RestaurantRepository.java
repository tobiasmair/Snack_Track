package edu.mci.snacktrack.repositories;

import edu.mci.snacktrack.model.Restaurant;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
    Optional<Restaurant> findByEmail(String email);

    @Query("SELECT r FROM Restaurant r LEFT JOIN FETCH r.menu WHERE r.restaurantId = :id")
    Optional<Restaurant> findByIdWithMenu(@Param("id") Long id);

}
