package raf.rs.reservations.service.impl;

import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import raf.rs.reservations.domain.Appointment;
import raf.rs.reservations.domain.Restaurant;
import raf.rs.reservations.domain.Table;
import raf.rs.reservations.dto.AppointmentCreateDto;
import raf.rs.reservations.dto.AppointmentDto;
import raf.rs.reservations.dto.RestaurantCreateDto;
import raf.rs.reservations.dto.RestaurantDto;
import raf.rs.reservations.dto.TableCreateDto;
import raf.rs.reservations.dto.TableDto;
import raf.rs.reservations.exception.InvalidDataException;
import raf.rs.reservations.exception.NotFoundException;
import raf.rs.reservations.repository.AppointmentRepository;
import raf.rs.reservations.repository.RestaurantRepository;
import raf.rs.reservations.repository.TableRepository;
import raf.rs.reservations.service.RestaurantService;

@Service
@Transactional
public class RestaurantServiceImpl implements RestaurantService {
    private final RestaurantRepository restaurantRepository;
    private final TableRepository tableRepository;
    private final AppointmentRepository appointmentRepository;
    private final ModelMapper modelMapper;

    public RestaurantServiceImpl(RestaurantRepository restaurantRepository, TableRepository tableRepository, AppointmentRepository appointmentRepository, ModelMapper modelMapper) {
        this.restaurantRepository = restaurantRepository;
        this.tableRepository = tableRepository;
        this.appointmentRepository = appointmentRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public Page<RestaurantDto> findAll(Pageable pageable) {
        return this.restaurantRepository
            .findAll(pageable)
            .map((restaurant) -> this.modelMapper.map(restaurant, RestaurantDto.class));
    }

    @Override
    public RestaurantDto createRestaurant(RestaurantCreateDto restaurantCreateDto) {
        final Restaurant restaurant = this.modelMapper.map(restaurantCreateDto, Restaurant.class);
        restaurant.setId(null);
        this.restaurantRepository.save(restaurant);
        return this.modelMapper.map(restaurant, RestaurantDto.class);
    }

    @Override
    public RestaurantDto updateRestaurant(RestaurantDto restaurantDto, Long id) {
        final Restaurant restaurant = this.restaurantRepository
            .findById(id)
            .orElseThrow(() -> new NotFoundException("Restaurant does not exist"));

        restaurant.setName(restaurantDto.getName());
        restaurant.setAddress(restaurantDto.getAddress());
        restaurant.setDescription(restaurantDto.getDescription());
        restaurant.setOpenTime(restaurantDto.getOpenTime());
        restaurant.setType(restaurantDto.getType());
        restaurant.setDiscountAfterXReservations(restaurantDto.getDiscountAfterXReservations());
        restaurant.setFreeItemEachXReservations(restaurantDto.getFreeItemEachXReservations());

        if (restaurantDto.getManagerId() != null) {
            restaurant.setManagerId(restaurantDto.getManagerId());
        }

        return this.modelMapper.map(this.restaurantRepository.save(restaurant), RestaurantDto.class);
    }

    @Override
    public AppointmentDto createAppointment(AppointmentCreateDto appointmentCreateDto) {
        if (appointmentCreateDto.getTime().isBefore(LocalDateTime.now())) {
            throw new InvalidDataException("Date must be after now");
        }

        final Table table = this.tableRepository
            .findById(appointmentCreateDto.getTableId())
            .orElseThrow(() -> new NotFoundException("Table does not exist"));

        final Appointment appointment = new Appointment();
        appointment.setTime(appointmentCreateDto.getTime());
        appointment.setTable(table);

        return this.modelMapper.map(this.appointmentRepository.save(appointment), AppointmentDto.class);
    }

    @Override
    public TableDto createTable(TableCreateDto dto) {
        final Restaurant restaurant = this.restaurantRepository
            .findById(dto.getRestaurantId())
            .orElseThrow(() -> new NotFoundException("Restaurant does not exist"));

        final Table table = new Table();
        table.setRestaurant(restaurant);
        table.setZone(dto.getZone());
        table.setCapacity(dto.getCapacity());

        this.tableRepository.save(table);
        return this.modelMapper.map(table, TableDto.class);
    }

    @Override
    public RestaurantDto findById(Long restaurantId) {
        return this.restaurantRepository
            .findById(restaurantId)
            .map((restaurant) -> this.modelMapper.map(restaurant, RestaurantDto.class))
            .orElseThrow(() -> new NotFoundException("Restaurant does not exist"));
    }
}
