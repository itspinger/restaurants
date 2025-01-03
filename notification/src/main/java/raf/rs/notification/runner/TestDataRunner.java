package raf.rs.notification.runner;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
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
        this.createType("Mail Activation", "Hello %s, please active your account by visiting the following link %s");
        this.createType("Password Reset", "Hello %s, you can change your password on this link %s");
        this.createType("Client created a reservation", "Hello, a user has just created a reservation at %s time and table %s");
        this.createType("Client created a reservation with benefits", "Hello, a user has just created a reservation at %s time and table %s with benefits");
        this.createType("Reservation Confirmed", "Hello %s, we're mailing you to confirm your reservation for restaurant %s at time %s and table %s");
        this.createType("Reservation Confirmed (With Benefits)", "Hello %s, we're mailing you to confirm your reservation for restaurant %s at time %s and table %s");
        this.createType("Client cancelled a reservation", "Hello, a user has just cancelled their reservation for table %s at time %s");
        this.createType("Manager cancelled your reservation", "We're sorry to inform you that the manager has cancelled your reservation for table %s at time %s");
        this.createType("Reservation Reminder", "Hello %s, we're mailing you to remind you that you have a reservation in an hour for restaurant %s at table %s");
    }

    private void createType(String name, String text) {
        final NotificationType notificationType = new NotificationType();
        notificationType.setName(name);
        notificationType.setText(text);
        this.notificationTypeRepository.save(notificationType);
    }
}
