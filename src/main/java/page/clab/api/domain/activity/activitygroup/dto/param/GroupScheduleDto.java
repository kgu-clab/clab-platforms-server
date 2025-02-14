package page.clab.api.domain.activity.activitygroup.dto.param;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class GroupScheduleDto {

    private LocalDateTime schedule;
    private String content;
}
