package raf.rs.restaurants.userservice.dto;

import jakarta.validation.constraints.Min;
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
public class TablesDto {
    private Long id;

    @NotNull
    @Min(value = 1)
    private Integer capacity;

    @NotNull
    @Size(min = 3, max = 20)
    private String zone;

    @NotNull
    private Boolean isAvailable;
    @NotNull
    private Boolean isBlocked;
}
