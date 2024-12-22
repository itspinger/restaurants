package raf.rs.restaurants.userservice.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReservationDto {
    private Long id;

    @NotNull
    private Long appointmentId;

    @NotNull
    private Long clientId;

    private String note;
    @NotNull

    private boolean isActive;
    @NotNull

    private String declinedBy;
}
