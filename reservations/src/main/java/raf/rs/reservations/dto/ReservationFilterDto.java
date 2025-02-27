package raf.rs.reservations.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReservationFilterDto {

    private Long userId;
    private Long restaurantId;
    private Boolean available;

}
