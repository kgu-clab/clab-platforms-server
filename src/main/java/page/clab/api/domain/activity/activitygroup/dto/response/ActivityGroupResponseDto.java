package page.clab.api.domain.activity.activitygroup.dto.response;

import lombok.Builder;
import lombok.Getter;
import page.clab.api.domain.activity.activitygroup.domain.ActivityGroup;
import page.clab.api.domain.activity.activitygroup.domain.ActivityGroupCategory;
import page.clab.api.domain.activity.activitygroup.domain.ActivityGroupStatus;

import java.time.LocalDateTime;

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

    public static ActivityGroupResponseDto toDto(ActivityGroup activityGroup) {
        return ActivityGroupResponseDto.builder()
                .id(activityGroup.getId())
                .name(activityGroup.getName())
                .category(activityGroup.getCategory())
                .subject(activityGroup.getSubject())
                .status(activityGroup.getStatus())
                .imageUrl(activityGroup.getImageUrl())
                .createdAt(activityGroup.getCreatedAt())
                .build();
    }
}
