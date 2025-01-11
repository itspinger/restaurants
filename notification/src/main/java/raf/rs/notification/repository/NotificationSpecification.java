package raf.rs.notification.repository;

import java.time.LocalDate;
import org.springframework.data.jpa.domain.Specification;
import raf.rs.notification.domain.Notification;
import raf.rs.notification.dto.NotificationFilterDto;

public class NotificationSpecification {

    public static Specification<Notification> from(NotificationFilterDto notification) {
        final NotificationSpecification spec = new NotificationSpecification();

        return Specification.where(spec.hasEmail(notification.getEmail()))
            .and(spec.hasNotificationId(notification.getType()))
            .and(spec.sentAfter(notification.getStartDate()))
            .and(spec.sentBefore(notification.getEndDate()));
    }

    private Specification<Notification> hasEmail(String email) {
        return (root, query, criteriaBuilder) -> {
            if (email == null) {
                return null;
            }

            return criteriaBuilder.like(criteriaBuilder.lower(root.get("email")), "%" + email.toLowerCase() + "%");
        };
    }

    private Specification<Notification> hasNotificationId(Long notificationTypeId) {
        return (root, query, criteriaBuilder) -> {
            if (notificationTypeId == null) {
                return null;
            }

            return criteriaBuilder.equal(root.get("notificationType").get("id"), notificationTypeId);
        };
    }

    private Specification<Notification> sentAfter(LocalDate date) {
        return (root, query, criteriaBuilder) -> {
            if (date == null) {
                return null;
            }

            return criteriaBuilder.greaterThanOrEqualTo(root.get("timestamp"), date.atStartOfDay());
        };
    }

    private Specification<Notification> sentBefore(LocalDate date) {
        return (root, query, criteriaBuilder) -> {
            if (date == null) {
                return null;
            }

            return criteriaBuilder.lessThanOrEqualTo(root.get("timestamp"), date.atTime(23, 59, 59));
        };
    }

}
