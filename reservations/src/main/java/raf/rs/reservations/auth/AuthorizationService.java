package raf.rs.reservations.auth;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import raf.rs.reservations.domain.Reservation;
import raf.rs.reservations.domain.Restaurant;
import raf.rs.reservations.exception.ForbiddenException;
import raf.rs.reservations.repository.RestaurantRepository;
import raf.rs.reservations.service.ReservationService;
import raf.rs.reservations.service.RestaurantService;
import raf.rs.reservations.user.UserDetailsImpl;

@Service
public class AuthorizationService {
    private final RestaurantRepository repository;
    private final ReservationService service;

    public AuthorizationService(RestaurantRepository repository, ReservationService service) {
        this.repository = repository;
        this.service = service;
    }

    public boolean isManager() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        final UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        return userDetails.hasRole("ROLE_MANAGER");
    }

    public boolean canCancelReservation(Authentication authentication, Long reservationId) {
        final UserDetailsImpl details = (UserDetailsImpl) authentication.getPrincipal();
        final Long userId = details.getUserId();
        final Reservation reservation = this.service.findReservationById(reservationId);
        if (!reservation.getUserId().equals(userId) && !userId.equals(this.getManagerId(reservation))) {
            throw new ForbiddenException("No permission to perform this action");
        }

        return true;
    }

    public boolean canUpdateRestaurant(Authentication authentication, Long restaurantId) {
        final UserDetailsImpl details = (UserDetailsImpl) authentication.getPrincipal();
        final Long userId = details.getUserId();
        final Restaurant restaurant = this.repository.findById(restaurantId)
            .orElseThrow(() -> new ForbiddenException("Restaurant not found"));

        if (!userId.equals(restaurant.getManagerId())) {
            throw new ForbiddenException("You are not the manager of this restaurant");
        }

        return true;
    }

    private Long getManagerId(Reservation reservation) {
        return reservation.getAppointment().getTable().getRestaurant().getManagerId();
    }

}

