package raf.rs.reservations.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentFilterDto {

    private String cuisineType;

    private String location;

    private int capacity;

    private LocalDateTime appointmentTime;

}
