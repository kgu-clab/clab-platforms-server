package page.clab.api.domain.activity.activitygroup.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import page.clab.api.domain.activity.activitygroup.domain.ActivityGroupCategory;

@Getter
@Builder
public class ActivityGroupStatusResponseDto {

    private Long id;
    private String name;
    private String content;
    private ActivityGroupCategory category;
    private String subject;
    private String imageUrl;
    private List<LeaderInfo> leaders;
    private Long participantCount;
    private Long weeklyActivityCount;
    private LocalDateTime createdAt;
}
