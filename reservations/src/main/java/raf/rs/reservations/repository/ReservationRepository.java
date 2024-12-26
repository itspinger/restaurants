package raf.rs.reservations.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import raf.rs.reservations.domain.Reservation;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {



}
