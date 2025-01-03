package raf.rs.reservations.auth;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import raf.rs.reservations.domain.Reservation;
import raf.rs.reservations.exception.ForbiddenException;
import raf.rs.reservations.service.ReservationService;
import raf.rs.reservations.user.UserDetailsImpl;

@Service
public class AuthorizationService {
    private final ReservationService service;

    public AuthorizationService(ReservationService service) {
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
        final Long restaurantId = details.getRestaurantId();
        final Reservation reservation = this.service.findReservationById(reservationId);
        if (!reservation.getUserId().equals(userId) && !this.getRestaurantId(reservation).equals(restaurantId)) {
            throw new ForbiddenException("No permission to perform this action");
        }

        return true;
    }

    private Long getRestaurantId(Reservation reservation) {
        return reservation.getAppointment().getTable().getRestaurant().getId();
    }

}

