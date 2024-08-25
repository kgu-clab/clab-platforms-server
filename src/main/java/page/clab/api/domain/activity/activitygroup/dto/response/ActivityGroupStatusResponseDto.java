package page.clab.api.domain.activity.activitygroup.dto.response;

import lombok.Builder;
import lombok.Getter;
import org.springframework.util.CollectionUtils;
import page.clab.api.domain.activity.activitygroup.domain.ActivityGroup;
import page.clab.api.domain.activity.activitygroup.domain.ActivityGroupCategory;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class ActivityGroupStatusResponseDto {

    private Long id;
    private String name;
    private String content;
    private ActivityGroupCategory category;
    private String subject;
    private String imageUrl;
    private List<LeaderInfo> leaders;
    private Long participantCount;
    private Long weeklyActivityCount;
    private LocalDateTime createdAt;

    public static ActivityGroupStatusResponseDto toDto(ActivityGroup activityGroup, List<LeaderInfo> leader, Long participantCount, Long weeklyActivityCount) {
        return ActivityGroupStatusResponseDto.builder()
                .id(activityGroup.getId())
                .name(activityGroup.getName())
                .content(activityGroup.getContent())
                .category(activityGroup.getCategory())
                .subject(activityGroup.getSubject())
                .imageUrl(activityGroup.getImageUrl())
                .leaders(CollectionUtils.isEmpty(leader) ? null : leader)
                .participantCount(participantCount)
                .weeklyActivityCount(weeklyActivityCount)
                .createdAt(activityGroup.getCreatedAt())
                .build();
    }
}
