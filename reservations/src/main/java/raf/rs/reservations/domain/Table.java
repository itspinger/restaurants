package raf.rs.reservations.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@jakarta.persistence.Table(name = "tables")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Table {
    @Id
    @GeneratedValue
    private Long id;

    private Integer capacity;
    private Zone zone; //mozda treba enum

    @ManyToOne
    private Restaurant restaurant;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "table", orphanRemoval = true)
    private List<Appointment> appointments=new ArrayList<>();

    public enum Zone {
        SMOKING,
        NO_SMOKING
    }
}
