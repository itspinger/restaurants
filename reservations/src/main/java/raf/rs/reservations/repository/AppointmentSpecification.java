package raf.rs.reservations.repository;

import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import org.springframework.data.jpa.domain.Specification;
import raf.rs.reservations.domain.Appointment;
import raf.rs.reservations.domain.Reservation;
import raf.rs.reservations.dto.AppointmentFilterDto;

public class AppointmentSpecification {

    public static Specification<Appointment> from(AppointmentFilterDto filterDto) {
        return Specification.where(isAvailable(filterDto.isAvailable()))
            .and(hasCuisineType(filterDto.getCuisineType()))
            .and(hasLocation(filterDto.getLocation()))
            .and(hasCapacity(filterDto.getCapacity()))
            .and(isDate(filterDto.getDate()))
            .and(isTime(filterDto.getTime()))
            .and(isTableId(filterDto.getTableId()));
    }

    // If onlyAvailable it fetches only available appointments
    private static Specification<Appointment> isAvailable(boolean onlyAvailable) {
        return (root, query, criteriaBuilder) -> {
            if (!onlyAvailable || query == null) {
                return null;
            }

            final Subquery<Long> subquery = query.subquery(Long.class);
            final Root<Reservation> reservationRoot = subquery.from(Reservation.class);

            subquery.select(reservationRoot.get("id"))
                .where(criteriaBuilder.equal(reservationRoot.get("appointment"), root));

            return criteriaBuilder.and(
                criteriaBuilder.not(criteriaBuilder.exists(subquery)),
                criteriaBuilder.greaterThan(root.get("time"), LocalDateTime.now())
            );
        };
    }

    private static Specification<Appointment> isTableId(Long tableId) {
        return (root, query, criteriaBuilder) -> {
            if (tableId == null) {
                return null;
            }

            return criteriaBuilder.equal(root.get("table").get("id"), tableId);
        };
    }

    public static Specification<Appointment> hasCuisineType(String cuisineType) {
        return (root, query, criteriaBuilder) -> {
            if (cuisineType == null) {
                return null;
            }

            // Appointment -> Table -> Restaurant -> Type
            return criteriaBuilder.equal(root.get("table").get("restaurant").get("type"), cuisineType);
        };
    }

    public static Specification<Appointment> hasLocation(String location) {
        return (root, query, criteriaBuilder) -> {
            if (location == null) {
                return null;
            }

            return criteriaBuilder.like(
                root.get("table").get("restaurant").get("address"),
                "%" + location + "%"
            );
        };
    }

    public static Specification<Appointment> hasCapacity(int numberOfPeople) {
        return (root, query, criteriaBuilder) -> {
            if (numberOfPeople <= 0) {
                return null;
            }

            return criteriaBuilder.greaterThanOrEqualTo(root.get("table").get("capacity"), numberOfPeople);
        };
    }

    public static Specification<Appointment> isDate(LocalDate dateTime) {
        return (root, query, criteriaBuilder) -> {
            if (dateTime == null) {
                return null;
            }

            return criteriaBuilder.equal(criteriaBuilder.function("DATE", LocalDate.class, root.get("time")), dateTime);
        };
    }

    public static Specification<Appointment> isTime(LocalTime time) {
        return (root, query, criteriaBuilder) -> {
            if (time == null) {
                return null;
            }

            return criteriaBuilder.and(
                criteriaBuilder.equal(criteriaBuilder.function("HOUR", Integer.class, root.get("time")), time.getHour()),
                criteriaBuilder.lessThanOrEqualTo(
                    criteriaBuilder.abs(
                        criteriaBuilder.diff(
                            criteriaBuilder.function("MINUTE", Integer.class, root.get("time")), time.getMinute()
                        )
                    ), 29
                )
            );
        };
    }

}
