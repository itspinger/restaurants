package raf.rs.reservations.service;

import jakarta.validation.Valid;
import java.util.List;
import raf.rs.reservations.domain.Appointment;
import raf.rs.reservations.dto.AppointmentCreateDto;
import raf.rs.reservations.dto.AppointmentDto;
import raf.rs.reservations.dto.RestaurantCreateDto;
import raf.rs.reservations.dto.RestaurantDto;
import raf.rs.reservations.dto.TableCreateDto;
import raf.rs.reservations.dto.TableDto;

public interface RestaurantService {

    List<RestaurantDto> findAll();

    RestaurantDto createRestaurant(RestaurantCreateDto restaurantCreateDto);

    RestaurantDto updateRestaurant(RestaurantDto restaurantDto, Long id);

    AppointmentDto createAppointment(AppointmentCreateDto appointment);

    TableDto createTable(TableCreateDto dto);
}
