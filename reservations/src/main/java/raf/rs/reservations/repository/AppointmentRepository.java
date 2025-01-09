package raf.rs.reservations.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import raf.rs.reservations.domain.Appointment;
import raf.rs.reservations.dto.AppointmentFilterDto;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long>, JpaSpecificationExecutor<Appointment> {

    default Page<Appointment> findAvailableAppointments(AppointmentFilterDto filterDto, Pageable pageable) {
        return this.findAll(AppointmentSpecification.from(filterDto), pageable);
    }

}
