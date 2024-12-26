package page.clab.api.domain.activity.activitygroup.dto.response;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ActivityGroupReportResponseDto {

    private Long activityGroupId;
    private String activityGroupName;
    private Long turn;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
