package page.clab.api.domain.activityGroup.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import page.clab.api.domain.activityGroup.domain.ActivityGroup;
import page.clab.api.domain.activityGroup.domain.ActivityGroupBoard;
import page.clab.api.domain.activityGroup.domain.ActivityGroupCategory;
import page.clab.api.domain.activityGroup.domain.ActivityGroupStatus;
import page.clab.api.domain.activityGroup.domain.GroupMember;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
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
        return ActivityGroupProjectResponseDto.builder()
                .id(activityGroup.getId())
                .category(activityGroup.getCategory())
                .subject(activityGroup.getSubject())
                .name(activityGroup.getName())
                .content(activityGroup.getContent())
                .status(activityGroup.getStatus())
                .imageUrl(activityGroup.getImageUrl())
                .groupMembers(groupMembers.stream().map(GroupMemberResponseDto::toDto).toList())
                .startDate(activityGroup.getStartDate())
                .endDate(activityGroup.getEndDate())
                .techStack(activityGroup.getTechStack())
                .githubUrl(activityGroup.getGithubUrl())
                .activityGroupBoards(boards.stream().map(ActivityGroupBoardResponseDto::toDto).toList())
                .isOwner(isOwner)
                .createdAt(activityGroup.getCreatedAt())
                .build();
    }

}