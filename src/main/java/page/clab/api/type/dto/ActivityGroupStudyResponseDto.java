package page.clab.api.type.dto;

import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import page.clab.api.type.entity.ActivityGroup;
import page.clab.api.type.etc.ActivityGroupCategory;
import page.clab.api.type.etc.ActivityGroupStatus;
import page.clab.api.util.ModelMapperUtil;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ActivityGroupStudyResponseDto {

    private ActivityGroupCategory category;

    private String name;

    private String content;

    private ActivityGroupStatus status;

    private String imageUrl;

    private List<GroupMemberResponseDto> groupMembers;

    private String curriculum;

    private LocalDateTime createdAt;

    public static ActivityGroupStudyResponseDto of(ActivityGroup activityGroup) {
        return ModelMapperUtil.getModelMapper().map(activityGroup, ActivityGroupStudyResponseDto.class);
    }
}