package raf.rs.restaurants.userservice.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;
import java.sql.Time;

@Entity
@Table(name = "notification")

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Notification {
    @Id
    @GeneratedValue
    private Long id;


    private String text;//ovde treba neki string build ili nesto i treabaju parametri nekako da se dobiju

    private String email;
    @OneToOne
    private NotificationType notificationType;
    private Date send_date;
    private Time send_tame;
}
