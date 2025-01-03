package raf.rs.reservations.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import raf.rs.reservations.domain.Restaurant;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
}
