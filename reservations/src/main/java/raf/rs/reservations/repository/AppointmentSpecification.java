package raf.rs.reservations.repository;

import java.time.LocalDateTime;
import org.springframework.data.jpa.domain.Specification;
import raf.rs.reservations.domain.Appointment;
import raf.rs.reservations.dto.AppointmentFilterDto;

public class AppointmentSpecification {

    public static Specification<Appointment> from(AppointmentFilterDto filterDto) {
        return Specification.where(hasCuisineType(filterDto.getCuisineType()))
            .and(hasLocation(filterDto.getLocation()))
            .and(hasCapacity(filterDto.getCapacity()))
            .and(startsAt(filterDto.getAppointmentTime()));
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

            return criteriaBuilder.equal(root.get("table").get("restaurant").get("address"), location);
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

    public static Specification<Appointment> startsAt(LocalDateTime dateTime) {
        return (root, query, criteriaBuilder) -> {
            if (dateTime == null) {
                return null;
            }

            return criteriaBuilder.equal(root.get("time"), dateTime);
        };
    }

}
