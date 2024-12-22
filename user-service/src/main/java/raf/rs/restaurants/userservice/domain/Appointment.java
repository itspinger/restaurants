package raf.rs.restaurants.userservice.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Time;
import java.util.Date;
import java.util.Timer;

@Entity
@Table(name = "appointment")
@Inheritance
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Appointment {
    @Id
    @GeneratedValue
    private Long id;

    private Date date;
    private Time time;
    @ManyToOne
    private Tables table;
}
