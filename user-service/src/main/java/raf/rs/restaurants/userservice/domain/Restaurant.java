package raf.rs.restaurants.userservice.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "restaurant")
@Inheritance
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
    private List<Tables> tables=new ArrayList<>();
    private Integer tables_num=tables.size();//TODO ovde treba da se uzme nekako size od liste stolova msm treba u nekom set ili konstuktoru to da se stavi vrv ne moze ovako
    private String open_time;
    private String type;


}