package raf.rs.reservations.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentFilterDto {

    private Long tableId;

    private String cuisineType;

    private String location;

    private int capacity;

    private LocalDate date;

    private LocalTime time;

    private boolean available;

}
