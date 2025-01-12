package raf.rs.reservations.service.impl;

import java.util.List;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import raf.rs.reservations.domain.Restaurant;
import raf.rs.reservations.dto.AppointmentDto;
import raf.rs.reservations.dto.AppointmentFilterDto;
import raf.rs.reservations.dto.FilteredAppointmentDto;
import raf.rs.reservations.dto.RestaurantDto;
import raf.rs.reservations.repository.AppointmentRepository;
import raf.rs.reservations.service.AppointmentService;

@Service
public class AppointmentServiceImpl implements AppointmentService {
    private final AppointmentRepository appointmentRepository;
    private final ModelMapper modelMapper;

    public AppointmentServiceImpl(AppointmentRepository appointmentRepository, ModelMapper modelMapper) {
        this.appointmentRepository = appointmentRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public Page<FilteredAppointmentDto> findAvailableAppointments(AppointmentFilterDto filterDto, Pageable pageable) {
        return this.appointmentRepository
            .findAvailableAppointments(filterDto, pageable)
            .map(appointment -> {
                final FilteredAppointmentDto map = this.modelMapper.map(appointment, FilteredAppointmentDto.class);
                final RestaurantDto restaurantDto = this.mapShallow(appointment.getTable().getRestaurant());
                map.setRestaurant(restaurantDto);
                return map;
            });
    }

    private RestaurantDto mapShallow(Restaurant restaurant) {
        // Ignore the tables, we will not serialize it
        restaurant.setTables(List.of());
        return this.modelMapper.map(restaurant, RestaurantDto.class);
    }


}
