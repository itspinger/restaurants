package raf.rs.reservations.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TableDto {
    private Long id;

    @NotNull
    @Min(value = 1)
    private Integer capacity;

    @NotNull
    @Size(min = 3, max = 20)
    private String zone;

    private RestaurantDto restaurantDto;
}
