package raf.rs.reservations.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import raf.rs.reservations.domain.Table;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TableCreateDto {

    @Min(1)
    private int capacity;

    @NotNull
    private Table.Zone zone;

    @NotNull
    private Long restaurantId;

}
