package page.clab.api.domain.activity.activitygroup.dto.response;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import page.clab.api.domain.activity.activitygroup.domain.ActivityGroupCategory;
import page.clab.api.domain.activity.activitygroup.domain.ActivityGroupStatus;

@Getter
@Builder
public class ActivityGroupResponseDto {

    private Long id;
    private String name;
    private ActivityGroupCategory category;
    private String subject;
    private ActivityGroupStatus status;
    private String imageUrl;
    private LocalDateTime createdAt;
}
