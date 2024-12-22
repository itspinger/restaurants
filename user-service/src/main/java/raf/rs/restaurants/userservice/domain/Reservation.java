package raf.rs.restaurants.userservice.domain;

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

    @ManyToOne
    private Client client;// ovo je moralo da bude zbog users tabele

    private String note;

    private boolean isActive;

    private UserType declinedBy;

}
