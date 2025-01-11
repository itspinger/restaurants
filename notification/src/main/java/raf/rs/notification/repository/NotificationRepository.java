package raf.rs.notification.repository;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import raf.rs.notification.domain.Notification;
import raf.rs.notification.dto.NotificationFilterDto;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long>, JpaSpecificationExecutor<Notification> {

    List<Notification> findByEmail(String email);

    default Page<Notification> findAll(NotificationFilterDto filterDto, Pageable pageable) {
        return this.findAll(NotificationSpecification.from(filterDto), pageable);
    }

}
