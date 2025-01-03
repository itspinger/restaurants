package raf.rs.reservations.service.impl;

import java.util.List;
import org.springframework.stereotype.Service;
import raf.rs.reservations.domain.Appointment;
import raf.rs.reservations.domain.Restaurant;
import raf.rs.reservations.dto.AppointmentDto;
import raf.rs.reservations.dto.RestaurantDto;
import raf.rs.reservations.exception.NotFoundException;
import raf.rs.reservations.mapper.AppointmentMapper;
import raf.rs.reservations.mapper.RestaurantMapper;
import raf.rs.reservations.repository.AppointmentRepository;
import raf.rs.reservations.repository.RestaurantRepository;
import raf.rs.reservations.service.RestaurantService;

@Service
public class RestaurantServiceImpl implements RestaurantService {
    private final RestaurantRepository restaurantRepository;
    private final AppointmentRepository appointmentRepository;

    public RestaurantServiceImpl(RestaurantRepository restaurantRepository, AppointmentRepository appointmentRepository) {
        this.restaurantRepository = restaurantRepository;
        this.appointmentRepository = appointmentRepository;
    }

    @Override
    public List<RestaurantDto> findAll() {
        return this.restaurantRepository
            .findAll()
            .stream()
            .map(RestaurantMapper::toDTO)
            .toList();
    }

    @Override
    public RestaurantDto updateRestaurant(RestaurantDto restaurantDto, Long id) {
        final Restaurant restaurant = this.restaurantRepository
            .findById(id)
            .orElseThrow(() -> new NotFoundException("Failed to find a restaurant with id %s".formatted(id)));

        restaurant.setName(restaurantDto.getName());
        restaurant.setAddress(restaurantDto.getAddress());
        restaurant.setDescription(restaurantDto.getDescription());
        restaurant.setOpen_time(restaurantDto.getOpenTime());
        restaurant.setType(restaurantDto.getType());

        return RestaurantMapper.toDTO(this.restaurantRepository.save(restaurant));
    }

    @Override
    public Appointment createAppointment(AppointmentDto appointment) {
        final Appointment entity = AppointmentMapper.toEntity(appointment);
        this.appointmentRepository.save(entity);
        return entity;
    }
}
