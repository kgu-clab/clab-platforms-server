package page.clab.api.domain.members.schedule.application.dto.response;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import page.clab.api.domain.members.schedule.domain.SchedulePriority;

@Getter
@Builder
public class ScheduleResponseDto {

    private Long id;
    private String title;
    private String detail;
    private String activityName;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private SchedulePriority priority;
}
