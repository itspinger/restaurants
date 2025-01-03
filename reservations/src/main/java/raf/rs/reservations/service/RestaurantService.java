package raf.rs.reservations.service;

import java.util.List;
import raf.rs.reservations.domain.Appointment;
import raf.rs.reservations.dto.AppointmentDto;
import raf.rs.reservations.dto.RestaurantDto;

public interface RestaurantService {

    List<RestaurantDto> findAll();

    RestaurantDto updateRestaurant(RestaurantDto restaurantDto, Long id);

    Appointment createAppointment(AppointmentDto appointment);

}
