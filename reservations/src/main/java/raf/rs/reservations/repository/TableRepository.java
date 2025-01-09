package raf.rs.reservations.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import raf.rs.reservations.domain.Table;

@Repository
public interface TableRepository extends JpaRepository<Table, Long> {

}
