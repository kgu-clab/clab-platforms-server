package page.clab.api.domain.activity.activitygroup.dto.response;

import lombok.Builder;
import lombok.Getter;
import page.clab.api.domain.activity.activitygroup.domain.ActivityGroupStatus;

@Getter
@Builder
public class ActivityGroupBoardStatusUpdatedResponseDto {

    private Long id;
    private ActivityGroupStatus activityGroupStatus;

    public static ActivityGroupBoardStatusUpdatedResponseDto toDto(Long groupId, ActivityGroupStatus activityGroupStatus) {
        return ActivityGroupBoardStatusUpdatedResponseDto.builder()
                .id(groupId)
                .activityGroupStatus(activityGroupStatus)
                .build();
    }
}