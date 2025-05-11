package edu.mci.snacktrack.service.implementation;

import edu.mci.snacktrack.dto.RestaurantDTO;
import edu.mci.snacktrack.mapper.RestaurantMapper;
import edu.mci.snacktrack.model.Restaurant;
import edu.mci.snacktrack.repositories.RestaurantRepository;
import edu.mci.snacktrack.service.RestaurantServiceInterface;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class RestaurantService implements RestaurantServiceInterface {

    private RestaurantRepository restaurantRepository;

    @Override
    public RestaurantDTO createRestaurant(RestaurantDTO restaurantDTO) {

        Restaurant restaurant = RestaurantMapper.mapToRestaurant(restaurantDTO);
        Restaurant savedRestaurant = restaurantRepository.save(restaurant);

        return RestaurantMapper.mapToRestaurantDTO(savedRestaurant);
    }
}
