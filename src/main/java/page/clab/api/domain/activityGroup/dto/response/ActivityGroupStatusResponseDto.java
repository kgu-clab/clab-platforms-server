package page.clab.api.domain.activityGroup.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import page.clab.api.domain.activityGroup.domain.ActivityGroup;
import page.clab.api.domain.activityGroup.domain.ActivityGroupCategory;
import page.clab.api.domain.member.domain.Member;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ActivityGroupStatusResponseDto {

    private Long id;

    private String name;

    private String content;

    private ActivityGroupCategory category;

    private String subject;

    private String imageUrl;

    private String leaderId;

    private String leaderName;

    private Long participantCount;

    private Long weeklyActivityCount;

    private LocalDateTime createdAt;

    public static ActivityGroupStatusResponseDto toDto(ActivityGroup activityGroup, Member leader, Long participantCount, Long weeklyActivityCount) {
        return ActivityGroupStatusResponseDto.builder()
                .id(activityGroup.getId())
                .name(activityGroup.getName())
                .content(activityGroup.getContent())
                .category(activityGroup.getCategory())
                .subject(activityGroup.getSubject())
                .imageUrl(activityGroup.getImageUrl())
                .leaderId(leader != null ? leader.getId() : null)
                .leaderName(leader != null ? leader.getName() : null)
                .participantCount(participantCount)
                .weeklyActivityCount(weeklyActivityCount)
                .createdAt(activityGroup.getCreatedAt())
                .build();
    }

}