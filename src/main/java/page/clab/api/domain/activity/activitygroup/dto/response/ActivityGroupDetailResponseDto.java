package page.clab.api.domain.activity.activitygroup.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import page.clab.api.domain.activity.activitygroup.domain.ActivityGroup;
import page.clab.api.domain.activity.activitygroup.domain.ActivityGroupCategory;
import page.clab.api.domain.activity.activitygroup.domain.ActivityGroupStatus;
import page.clab.api.domain.activity.activitygroup.domain.GroupMember;

@Getter
@Builder
public class ActivityGroupDetailResponseDto {

    private Long id;
    private ActivityGroupCategory category;
    private String subject;
    private String name;
    private String content;
    private ActivityGroupStatus status;
    private Long progress;
    private String imageUrl;
    private String curriculum;
    private List<GroupMemberResponseDto> groupMembers;
    private LocalDate startDate;
    private LocalDate endDate;
    private String techStack;
    private String githubUrl;
    private List<ActivityGroupBoardResponseDto> activityGroupBoards;

    @JsonProperty("isOwner")
    private boolean isOwner;
    private LocalDateTime createdAt;

    public static ActivityGroupDetailResponseDto create(ActivityGroup activityGroup,
                                                         List<ActivityGroupBoardResponseDto> boards, List<GroupMemberResponseDto> groupMemberResponseDtos, boolean isOwner) {
        return ActivityGroupDetailResponseDto.builder()
                .id(activityGroup.getId())
                .category(activityGroup.getCategory())
                .subject(activityGroup.getSubject())
                .name(activityGroup.getName())
                .content(activityGroup.getContent())
                .status(activityGroup.getStatus())
                .progress(activityGroup.getProgress())
                .imageUrl(activityGroup.getImageUrl())
                .curriculum(activityGroup.getCurriculum())
                .groupMembers(groupMemberResponseDtos)
                .startDate(activityGroup.getStartDate())
                .endDate(activityGroup.getEndDate())
                .techStack(activityGroup.getTechStack())
                .githubUrl(activityGroup.getGithubUrl())
                .activityGroupBoards(boards)
                .isOwner(isOwner)
                .createdAt(activityGroup.getCreatedAt())
                .build();
    }
}
