package raf.rs.reservations.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import raf.rs.reservations.domain.Table;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TableDto {
    private Long id;

    @Min(1)
    private int capacity;

    private Table.Zone zone;
}
