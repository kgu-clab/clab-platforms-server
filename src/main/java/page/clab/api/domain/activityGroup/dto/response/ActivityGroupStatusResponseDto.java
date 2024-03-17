package page.clab.api.domain.activityGroup.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import page.clab.api.domain.activityGroup.domain.ActivityGroup;
import page.clab.api.domain.activityGroup.domain.ActivityGroupCategory;
import page.clab.api.domain.member.domain.Member;
import page.clab.api.global.util.ModelMapperUtil;

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

    public static ActivityGroupStatusResponseDto create(ActivityGroup activityGroup, Member leader, Long participantCount, Long weeklyActivityCount) {
        ActivityGroupStatusResponseDto activityGroupResponseDto = ModelMapperUtil.getModelMapper().map(activityGroup, ActivityGroupStatusResponseDto.class);
        if (leader != null) {
            activityGroupResponseDto.setLeaderId(leader.getId());
            activityGroupResponseDto.setLeaderName(leader.getName());
        }
        activityGroupResponseDto.setParticipantCount(participantCount);
        activityGroupResponseDto.setWeeklyActivityCount(weeklyActivityCount);
        return activityGroupResponseDto;
    }

}