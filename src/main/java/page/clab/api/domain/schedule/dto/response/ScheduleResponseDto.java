package page.clab.api.domain.schedule.dto.response;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import page.clab.api.domain.schedule.domain.Schedule;
import page.clab.api.domain.schedule.domain.ScheduleType;
import page.clab.api.global.util.ModelMapperUtil;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ScheduleResponseDto {

    private Long id;

    private String title;

    private String detail;

    private String activityName;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    public static ScheduleResponseDto of(Schedule schedule) {
        ScheduleResponseDto scheduleResponseDto = ModelMapperUtil.getModelMapper()
                .map(schedule, ScheduleResponseDto.class);

        if (schedule.getScheduleType() != ScheduleType.ALL) {
            scheduleResponseDto.setActivityName(schedule.getActivityGroup().getName());
        }

        return scheduleResponseDto;
    }

}
