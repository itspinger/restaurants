package raf.rs.reservations.mapper;



import raf.rs.reservations.domain.Restaurant;
import raf.rs.reservations.dto.RestaurantDto;
import raf.rs.reservations.dto.TablesDto;

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
