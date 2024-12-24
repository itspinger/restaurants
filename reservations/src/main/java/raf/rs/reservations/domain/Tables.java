package raf.rs.reservations.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tables")

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Tables {
    @Id
    @GeneratedValue
    private Long id;

    private Integer capacity;
    private String zone; //mozda treba enum
    private Boolean isAvailable;
    @ManyToOne
    private Restaurant restaurant;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "table", orphanRemoval = true)
    private List<Appointment> appointments=new ArrayList<>();
    private Boolean isBlocked;



}