package raf.rs.notification.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "notifications")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Notification {
    @Id
    @GeneratedValue
    private Long id;

    // The text without placeholders which is going to be sent
    private String text;

    // The email which this notification is being sent to
    private String email;

    @ManyToOne
    private NotificationType notificationType;

    // The time the notification was sent at
    private LocalDateTime timestamp;
}
