package raf.rs.reservations.mapper;



import raf.rs.reservations.domain.Restaurant;
import raf.rs.reservations.dto.RestaurantDto;
import raf.rs.reservations.dto.TableDto;

import java.util.List;

public class RestaurantMapper {
    public static RestaurantDto toDTO(Restaurant restaurant) {
        // Convert List<Table> to List<TableDTO>
        List<TableDto> tableDTOs = TableMapper.toDTOList(restaurant.getTables());

        // Calculate tablesNum
        Integer tablesNum = restaurant.getTables().size();

        // Return RestaurantDTO
        return new RestaurantDto(
                restaurant.getId(),
                restaurant.getName(),
                restaurant.getAddress(),
                restaurant.getDescription(),
                tablesNum,
                restaurant.getOpen_time(),
                restaurant.getType(),
                tableDTOs
        );
    }

    public static Restaurant toEntity(RestaurantDto restaurantDto) {
        final Restaurant restaurant = new Restaurant();

        restaurant.setId(restaurantDto.getId());
        restaurant.setName(restaurantDto.getName());
        restaurant.setAddress(restaurantDto.getAddress());
        restaurant.setDescription(restaurantDto.getDescription());

        return restaurant;
    }
}
