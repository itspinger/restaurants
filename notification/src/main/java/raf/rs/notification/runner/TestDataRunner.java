package raf.rs.notification.runner;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import raf.rs.notification.domain.NotificationCategory;
import raf.rs.notification.domain.NotificationType;
import raf.rs.notification.repository.NotificationTypeRepository;

@Profile({"default"})
@Component
public class TestDataRunner implements CommandLineRunner {
    private final NotificationTypeRepository notificationTypeRepository;

    public TestDataRunner(NotificationTypeRepository notificationTypeRepository) {
        this.notificationTypeRepository = notificationTypeRepository;
    }

    @Override
    public void run(String... args) {
        this.createType(NotificationCategory.EMAIL_VERIFICATION, "Email Verification", "Hello %s, please active your account by visiting the following link %s");
        this.createType(NotificationCategory.RESET_PASSWORD, "Password Reset", "Hello %s, you can change your password on this link %s");

        this.createType(
            NotificationCategory.CLIENT_RESERVATION_CREATE,
            "Client created a reservation",
            "Hello %s, a user has just created a reservation at %s time and table %s"
        );

        this.createType(
            NotificationCategory.CLIENT_RESERVATION_CREATE_BENEFITS,
            "Client created a reservation with benefits",
            "Hello %s, a user has just created a reservation at %s time and table %s with benefits"
        );

        this.createType(
            NotificationCategory.RESERVATION_CONFIRM,
            "Reservation Confirmed",
            "Hello %s, we're mailing you to confirm your reservation for restaurant %s at time %s and table %s"
        );

        this.createType(
            NotificationCategory.RESERVATION_CONFIRM_BENEFITS,
            "Reservation Confirmed (With Benefits)",
            "Hello %s, we're mailing you to confirm your reservation for restaurant %s at time %s and table %s"
        );

        this.createType(
            NotificationCategory.CLIENT_CANCELLATION,
            "Client cancelled a reservation",
            "Hello %s, a user has just cancelled their reservation for table %s at time %s"
        );

        this.createType(
            NotificationCategory.MANAGER_CANCELLATION,
            "Manager cancelled your reservation",
            "We're sorry to inform you that the manager has cancelled your reservation at restaurant %s at time %s"
        );

        this.createType(
            NotificationCategory.RESERVATION_REMINDER,
            "Reservation Reminder",
            "Hello %s, we're mailing you to remind you that you have a reservation in an hour at table %s in restaurant %s"
        );
    }

    private void createType(NotificationCategory category, String name, String text) {
        if (this.notificationTypeRepository.findByCategory(category).isPresent()) {
            return;
        }

        final NotificationType notificationType = new NotificationType();
        notificationType.setCategory(category);
        notificationType.setName(name);
        notificationType.setText(text);
        this.notificationTypeRepository.save(notificationType);
    }
}
