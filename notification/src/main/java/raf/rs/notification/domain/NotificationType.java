package raf.rs.notification.domain;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Table(name = "notification_type")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NotificationType {
    @Id
    @GeneratedValue
    private Long id;

    @Column(unique=true)
    private String name;

    // Text with placeholders which need to be replaced and sent to the user
    // For example: “Pozdrav %ime %prezime. Vaša rezervacija za %datum u %vreme je potvrđena”
    private String text;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "notificationType", orphanRemoval = true)
    private List<Notification> notifications = new ArrayList<>();
}
