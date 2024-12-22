package raf.rs.restaurants.userservice.mapper;

import raf.rs.restaurants.userservice.domain.Restaurant;
import raf.rs.restaurants.userservice.dto.RestaurantDto;
import raf.rs.restaurants.userservice.dto.TablesDto;

import java.util.List;

public class RestaurantMapper {
    public static RestaurantDto toDTO(Restaurant restaurant) {
        // Convert List<Tables> to List<TableDTO>
        List<TablesDto> tableDTOs = TableMapper.toDTOList(restaurant.getTables());

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
}
