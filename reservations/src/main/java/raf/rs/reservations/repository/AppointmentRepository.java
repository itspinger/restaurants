package raf.rs.reservations.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import raf.rs.reservations.domain.Appointment;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
}
