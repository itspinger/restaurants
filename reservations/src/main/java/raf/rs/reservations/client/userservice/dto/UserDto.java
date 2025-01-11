package raf.rs.reservations.client.userservice.dto;

import java.time.LocalDate;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    private Long id;

    private String firstName;

    private String lastName;

    private String email;

    private String username;

    private Integer reservationCount;

    private LocalDate birthDate;

    private Long restaurantId;

    private Date startDate;

}
