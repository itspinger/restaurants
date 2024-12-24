package raf.rs.reservations.mapper;


import raf.rs.reservations.domain.Tables;
import raf.rs.reservations.dto.TablesDto;

import java.util.List;
import java.util.stream.Collectors;

public class TableMapper {
    public static TablesDto toDTO(Tables table) {
        return new TablesDto(
                table.getId(),
                table.getCapacity(),
                table.getZone(),
                table.getIsAvailable(),
                table.getIsBlocked()
        );
    }


    public static List<TablesDto> toDTOList(List<Tables> tables) {
        return tables.stream()
                .map(TableMapper::toDTO)
                .collect(Collectors.toList());
    }
}
