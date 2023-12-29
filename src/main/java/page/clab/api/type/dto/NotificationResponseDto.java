package page.clab.api.type.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import page.clab.api.type.entity.Notification;
import page.clab.api.util.ModelMapperUtil;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NotificationResponseDto {

    private Long id;

    private String content;

    private LocalDateTime createdAt;

    public static NotificationResponseDto of(Notification notification) {
        return ModelMapperUtil.getModelMapper().map(notification, NotificationResponseDto.class);
    }

}
