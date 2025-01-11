package raf.rs.notification.dto;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NotificationFilterDto {

    private Long type;

    private String email;

    private LocalDate startDate;

    private LocalDate endDate;

}
