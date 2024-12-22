package raf.rs.restaurants.userservice.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantDto {
    private Long id;

    @NotNull
    @Size(min = 3, max = 100)
    private String name;

    @NotNull
    @Size(min = 5, max = 200)
    private String address;

    private String description;

    @NotNull
    private Integer tablesNum;

    @NotNull
    private String openTime;

    @NotNull
    @Size(min = 3, max = 50)
    private String type;

    @NotNull
    private List<TablesDto> tables;
}
