package raf.rs.restaurants.userservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PasswordDto {

    @NotBlank
    @NotNull
    @Size(min = 8, max = 128, message = "Password must be between 8 and 128 characters")
    private String password;

}
