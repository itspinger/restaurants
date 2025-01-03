package raf.rs.restaurants.userservice.domain;

import jakarta.persistence.Entity;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Manager extends User {

    private Long restaurantId;
    private Date startDate;

}
