package raf.rs.reservations.domain;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.persistence.*;

import java.util.Date;

@Entity
@jakarta.persistence.Table(name = "appointment", uniqueConstraints =  @UniqueConstraint(columnNames = {"time", "table_id"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Appointment {
    @Id
    @GeneratedValue
    private Long id;

    private LocalDateTime time;

    @ManyToOne
    @JoinColumn(name = "table_id", nullable = false)
    private Table table;
}
