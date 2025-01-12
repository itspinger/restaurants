package raf.rs.reservations.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FilteredAppointmentDto extends AppointmentDto {

    private RestaurantDto restaurant;

}
