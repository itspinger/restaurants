package raf.rs.reservations.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "reservation")

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Reservation {
    @Id
    @GeneratedValue
    private Long id;

    @OneToOne
    private Appointment appointment;

    private String note;

    private boolean isActive;

    //treba nesto za ono koji user je otkazao

}
