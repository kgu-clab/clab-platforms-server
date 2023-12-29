package page.clab.api.type.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import page.clab.api.type.entity.ActivityGroup;
import page.clab.api.type.etc.ActivityGroupCategory;
import page.clab.api.type.etc.ActivityGroupStatus;
import page.clab.api.util.ModelMapperUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ActivityGroupProjectResponseDto {

    private ActivityGroupCategory category;

    private String name;

    private String content;

    private ActivityGroupStatus status;

    private String imageUrl;

    private List<GroupMemberResponseDto> groupMembers;

    private LocalDate startDate;

    private LocalDate endDate;

    private String techStack;

    private String githubUrl;

    private LocalDateTime createdAt;

    public static ActivityGroupProjectResponseDto of(ActivityGroup activityGroup) {
        return ModelMapperUtil.getModelMapper().map(activityGroup, ActivityGroupProjectResponseDto.class);
    }
}