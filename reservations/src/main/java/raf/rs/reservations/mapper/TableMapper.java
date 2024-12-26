package raf.rs.reservations.mapper;


import raf.rs.reservations.domain.Table;
import raf.rs.reservations.dto.TableDto;

import java.util.List;
import java.util.stream.Collectors;

public class TableMapper {

    public static TableDto toDTO(Table table) {
        return new TableDto(
            table.getId(),
            table.getCapacity(),
            table.getZone(),
            RestaurantMapper.toDTO(table.getRestaurant())
        );
    }

    public static Table toEntity(TableDto dto) {
        final Table table = new Table();
        table.setId(dto.getId());
        table.setCapacity(dto.getCapacity());
        table.setZone(dto.getZone());
        table.setRestaurant(RestaurantMapper.toEntity(dto.getRestaurantDto()));

        return table;
    }

    public static List<TableDto> toDTOList(List<Table> tables) {
        return tables.stream()
                .map(TableMapper::toDTO)
                .collect(Collectors.toList());
    }
}
