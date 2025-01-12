package raf.rs.reservations.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import raf.rs.reservations.domain.Reservation;
import raf.rs.reservations.dto.ReservationFilterDto;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long>, JpaSpecificationExecutor<Reservation> {

    boolean existsByAppointment_Id(Long appointmentId);

    default Page<Reservation> findAvailableReservations(ReservationFilterDto filterDto, Pageable pageable) {
        return this.findAll(ReservationSpecification.from(filterDto), pageable);
    }

}
