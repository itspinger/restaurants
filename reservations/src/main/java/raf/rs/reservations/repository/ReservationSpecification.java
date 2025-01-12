package raf.rs.reservations.repository;

import java.time.LocalDateTime;
import org.springframework.data.jpa.domain.Specification;
import raf.rs.reservations.domain.Reservation;
import raf.rs.reservations.dto.ReservationFilterDto;

public class ReservationSpecification {

    public static Specification<Reservation> from(ReservationFilterDto filterDto) {
        return Specification.where(isUserId(filterDto.getUserId()))
            .and(isRestaurantId(filterDto.getRestaurantId()))
            .and(isTimeAvailable(filterDto.getAvailable()));
    }

    private static Specification<Reservation> isUserId(Long userId) {
        return (root, query, criteriaBuilder) -> {
            if (userId == null) {
                return null;
            }

            return criteriaBuilder.equal(root.get("userId"), userId);
        };
    };

    private static Specification<Reservation> isRestaurantId(Long restaurantId) {
        return (root, query, criteriaBuilder) -> {
            if (restaurantId == null) {
                return null;
            }

            return criteriaBuilder.equal(root.get("appointment").get("table").get("restaurant").get("id"), restaurantId);
        };
    }

    private static Specification<Reservation> isTimeAvailable(Boolean available) {
        return (root, query, criteriaBuilder) -> {
            if (available == null) {
                return null;
            }

            final LocalDateTime currentTime = LocalDateTime.now(); // Use LocalDateTime for flexibility
            return criteriaBuilder.greaterThanOrEqualTo(root.get("appointment").get("time"), currentTime);
        };
    }

}
