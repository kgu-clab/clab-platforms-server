package page.clab.api.domain.activityGroup.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import page.clab.api.domain.activityGroup.domain.ActivityGroup;
import page.clab.api.domain.activityGroup.domain.ActivityGroupCategory;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class ActivityGroupResponseDto {

    private Long id;

    private String name;

    private ActivityGroupCategory category;

    private String subject;

    private String imageUrl;

    private LocalDateTime createdAt;

    public static ActivityGroupResponseDto toDto(ActivityGroup activityGroup) {
        return ActivityGroupResponseDto.builder()
                .id(activityGroup.getId())
                .name(activityGroup.getName())
                .category(activityGroup.getCategory())
                .subject(activityGroup.getSubject())
                .imageUrl(activityGroup.getImageUrl())
                .createdAt(activityGroup.getCreatedAt())
                .build();
    }

}