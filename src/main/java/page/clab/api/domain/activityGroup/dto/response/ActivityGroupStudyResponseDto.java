package page.clab.api.domain.activityGroup.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import page.clab.api.domain.activityGroup.domain.ActivityGroup;
import page.clab.api.domain.activityGroup.domain.ActivityGroupCategory;
import page.clab.api.domain.activityGroup.domain.ActivityGroupStatus;
import page.clab.api.global.util.ModelMapperUtil;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ActivityGroupStudyResponseDto {

    private Long id;

    private ActivityGroupCategory category;

    private String subject;

    private String name;

    private String content;

    private ActivityGroupStatus status;

    private String imageUrl;

    private List<GroupMemberResponseDto> groupMembers;

    private String curriculum;

    private List<ActivityGroupBoardResponseDto> activityGroupBoards;

    @JsonProperty("isOwner")
    private boolean isOwner;

    private LocalDateTime createdAt;

    public static ActivityGroupStudyResponseDto of(ActivityGroup activityGroup, List<GroupMemberResponseDto> groupMembers, List<ActivityGroupBoardResponseDto> noticeAndWeeklyActivityBoards, boolean isOwner) {
        ActivityGroupStudyResponseDto activityGroupStudyResponseDto = ModelMapperUtil.getModelMapper().map(activityGroup, ActivityGroupStudyResponseDto.class);
        activityGroupStudyResponseDto.setGroupMembers(groupMembers);
        activityGroupStudyResponseDto.setActivityGroupBoards(noticeAndWeeklyActivityBoards);
        activityGroupStudyResponseDto.setOwner(isOwner);
        return activityGroupStudyResponseDto;
    }

}