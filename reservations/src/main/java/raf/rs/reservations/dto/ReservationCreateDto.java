package raf.rs.reservations.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReservationCreateDto {

    private Long reservationId;
    private Long userId;
    private AppointmentDto appointment;

}
