package raf.rs.restaurants.userservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import raf.rs.restaurants.userservice.exception.CustomException;
import raf.rs.restaurants.userservice.exception.ErrorCode;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserPatchDto {

    @NotBlank(message = "Username is required")
    private String username;

    @Size(min = 8, max = 128, message = "Password must be between 8 and 128 characters")
    private String password;

    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    @Email(message = "Email field must be a valid email")
    private String email;

    @NotBlank(message = "Date of Birth is required")
    private String dateOfBirth;

    private Long restaurantId;

    private String startDate;

    public LocalDate validateDate(String date) {
        if (date.isEmpty()) {
            throw new CustomException("Date field must not be empty", ErrorCode.INVALID_DATA, HttpStatus.BAD_REQUEST);
        }

        try {
            return LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        } catch (Exception e) {
            throw new CustomException("Date Field is not in yyyy-MM-dd format", ErrorCode.INVALID_DATA, HttpStatus.BAD_REQUEST);
        }
    }
}
