package raf.rs.reservations.service.impl;

import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import raf.rs.reservations.client.userservice.dto.UserDto;
import raf.rs.reservations.domain.Reservation;
import raf.rs.reservations.dto.ReservationCreateDto;
import raf.rs.reservations.exception.NotFoundException;
import raf.rs.reservations.mapper.ReservationMapper;
import raf.rs.reservations.repository.ReservationRepository;
import raf.rs.reservations.service.ReservationService;

@Service
public class ReservationServiceImpl implements ReservationService {
    private final ReservationRepository reservationRepository;
    private final RestTemplate userServiceRestTemplate;

    public ReservationServiceImpl(ReservationRepository reservationRepository, RestTemplate userServiceRestTemplate) {
        this.reservationRepository = reservationRepository;
        this.userServiceRestTemplate = userServiceRestTemplate;
    }

    @Override
    public void createReservation(ReservationCreateDto reservationCreateDto) {
        final ResponseEntity<UserDto> responseEntity;
        try {
            responseEntity = this.userServiceRestTemplate.exchange("/user/%s".formatted(reservationCreateDto.getUserId()), HttpMethod.GET, null, UserDto.class);
        } catch (HttpClientErrorException e) {
            throw new NotFoundException("Error");
        }

        System.out.println(responseEntity.getBody().getReservationsNum());

        final Reservation reservation = ReservationMapper.toEntity(reservationCreateDto);
        this.reservationRepository.save(reservation);
    }
}
