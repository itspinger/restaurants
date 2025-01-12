package raf.rs.reservations.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantDto {
    private Long id;

    @NotNull
    @Size(min = 3, max = 100)
    private String name;

    @NotNull
    @Size(min = 5, max = 200)
    private String address;

    @NotNull
    @Size(min = 20, max = 200)
    private String description;

    @NotNull
    @NotEmpty
    private String openTime;

    @NotNull
    @Size(min = 3, max = 50)
    private String type;

    private List<TableDto> tables;

    @Min(-1)
    @NotNull
    private int discountAfterXReservations;

    @Min(-1)
    @NotNull
    private int freeItemEachXReservations;

    @NotNull
    private Long managerId;
}
