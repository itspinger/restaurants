package raf.rs.notification.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import raf.rs.notification.domain.NotificationCategory;
import raf.rs.notification.domain.NotificationType;

@Repository
public interface NotificationTypeRepository extends JpaRepository<NotificationType, Long> {

    Optional<NotificationType> findByCategory(NotificationCategory category);

}
