package page.clab.api.type.dto;

import java.time.LocalDateTime;
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
public class ActivityGroupDetailsResponseDto {

    private ActivityGroupCategory category;

    private String name;

    private String content;

    private ActivityGroupStatus status;

    private Long progress;

    private String imageUrl;

    private LocalDateTime createdAt;

    public static ActivityGroupDetailsResponseDto of(ActivityGroup activityGroup) {
        return ModelMapperUtil.getModelMapper().map(activityGroup, ActivityGroupDetailsResponseDto.class);
    }
}