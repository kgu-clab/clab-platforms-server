package page.clab.api.domain.activityGroup.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import page.clab.api.domain.activityGroup.domain.ActivityGroup;
import page.clab.api.domain.activityGroup.domain.ActivityGroupBoard;
import page.clab.api.domain.activityGroup.domain.ActivityGroupCategory;
import page.clab.api.domain.activityGroup.domain.ActivityGroupStatus;
import page.clab.api.domain.activityGroup.domain.GroupMember;
import page.clab.api.global.util.ModelMapperUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ActivityGroupProjectResponseDto {

    private Long id;

    private ActivityGroupCategory category;

    private String subject;

    private String name;

    private String content;

    private ActivityGroupStatus status;

    private String imageUrl;

    private List<GroupMemberResponseDto> groupMembers;

    private LocalDate startDate;

    private LocalDate endDate;

    private String techStack;

    private String githubUrl;

    private List<ActivityGroupBoardResponseDto> activityGroupBoards;

    @JsonProperty("isOwner")
    private boolean isOwner;

    private LocalDateTime createdAt;

    public static ActivityGroupProjectResponseDto create(ActivityGroup activityGroup, List<GroupMember> groupMembers, List<ActivityGroupBoard> boards, boolean isOwner) {
        ActivityGroupProjectResponseDto activityGroupProjectResponseDto = ModelMapperUtil.getModelMapper().map(activityGroup, ActivityGroupProjectResponseDto.class);
        activityGroupProjectResponseDto.setGroupMembers(groupMembers.stream().map(GroupMemberResponseDto::of).toList());
        activityGroupProjectResponseDto.setActivityGroupBoards(boards.stream().map(ActivityGroupBoardResponseDto::of).toList());
        activityGroupProjectResponseDto.setOwner(isOwner);
        return activityGroupProjectResponseDto;
    }

}