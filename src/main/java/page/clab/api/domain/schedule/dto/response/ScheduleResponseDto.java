package page.clab.api.domain.schedule.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import page.clab.api.domain.schedule.domain.Schedule;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class ScheduleResponseDto {

    private Long id;

    private String title;

    private String detail;

    private String activityName;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    public static ScheduleResponseDto toDto(Schedule schedule) {
        return ScheduleResponseDto.builder()
                .id(schedule.getId())
                .title(schedule.getTitle())
                .detail(schedule.getDetail())
                .activityName(schedule.isAllSchedule() ? null : schedule.getActivityGroup().getName())
                .startDate(schedule.getStartDateTime())
                .endDate(schedule.getEndDateTime())
                .build();
    }

}
