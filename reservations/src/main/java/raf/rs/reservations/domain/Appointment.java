package raf.rs.reservations.domain;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.persistence.*;
import java.sql.Time;
import java.util.Date;

@Entity
@jakarta.persistence.Table(name = "appointment")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Appointment {
    @Id
    @GeneratedValue
    private Long id;

    private Date date;

    private LocalDateTime time;

    @ManyToOne
    private Table table;
}
