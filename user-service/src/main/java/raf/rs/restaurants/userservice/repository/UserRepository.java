package raf.rs.restaurants.userservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import raf.rs.restaurants.userservice.domain.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    

}
