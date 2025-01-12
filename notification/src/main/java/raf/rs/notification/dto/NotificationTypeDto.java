package raf.rs.notification.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import raf.rs.notification.domain.NotificationCategory;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NotificationTypeDto {

    private Long id;
    private NotificationCategory category;
    private String name;
    private String text;

}
