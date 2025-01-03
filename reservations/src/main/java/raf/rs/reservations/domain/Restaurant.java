package raf.rs.reservations.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@jakarta.persistence.Table(name = "restaurant")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Restaurant {

    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String address;
    private String description;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "restaurant", orphanRemoval = true)
    private List<Table> tables=new ArrayList<>();
    private String open_time;
    private String type;
    private int discountAfterXReservations;
    private int freeItemEachXReservations;


}
