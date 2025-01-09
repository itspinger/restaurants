package raf.rs.reservations.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReservationCreateDto {

    @NotNull
    private Long userId;

    @NotNull
    private Long appointmentId;

}
