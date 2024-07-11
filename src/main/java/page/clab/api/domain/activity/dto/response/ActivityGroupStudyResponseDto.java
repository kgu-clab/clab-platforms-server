package page.clab.api.domain.activity.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import page.clab.api.domain.activity.domain.ActivityGroup;
import page.clab.api.domain.activity.domain.ActivityGroupBoard;
import page.clab.api.domain.activity.domain.ActivityGroupCategory;
import page.clab.api.domain.activity.domain.ActivityGroupStatus;
import page.clab.api.domain.activity.domain.GroupMember;

import java.time.LocalDateTime;
import java.util.List;

@Getter
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

    public static ActivityGroupStudyResponseDto create(ActivityGroup activityGroup, List<GroupMember> groupMembers, List<ActivityGroupBoard> boards, List<GroupMemberResponseDto> groupMemberResponseDtos, boolean isOwner) {
        return ActivityGroupStudyResponseDto.builder()
                .id(activityGroup.getId())
                .category(activityGroup.getCategory())
                .subject(activityGroup.getSubject())
                .name(activityGroup.getName())
                .content(activityGroup.getContent())
                .status(activityGroup.getStatus())
                .imageUrl(activityGroup.getImageUrl())
                .groupMembers(groupMemberResponseDtos)
                .curriculum(activityGroup.getCurriculum())
                .activityGroupBoards(boards.stream().map(ActivityGroupBoardResponseDto::toDto).toList())
                .isOwner(isOwner)
                .createdAt(activityGroup.getCreatedAt())
                .build();
    }

}
