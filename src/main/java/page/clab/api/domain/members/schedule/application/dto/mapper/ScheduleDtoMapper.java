package page.clab.api.domain.members.schedule.application.dto.mapper;

import org.springframework.stereotype.Component;
import page.clab.api.domain.activity.activitygroup.domain.ActivityGroup;
import page.clab.api.domain.members.schedule.application.dto.request.ScheduleRequestDto;
import page.clab.api.domain.members.schedule.application.dto.response.ScheduleCollectResponseDto;
import page.clab.api.domain.members.schedule.application.dto.response.ScheduleResponseDto;
import page.clab.api.domain.members.schedule.domain.Schedule;

@Component
public class ScheduleDtoMapper {

    public Schedule fromDto(ScheduleRequestDto requestDto, String memberId, ActivityGroup activityGroup) {
        return Schedule.builder()
            .scheduleType(requestDto.getScheduleType())
            .title(requestDto.getTitle())
            .detail(requestDto.getDetail())
            .startDateTime(requestDto.getStartDateTime())
            .endDateTime(requestDto.getEndDateTime())
            .priority(requestDto.getPriority())
            .scheduleWriter(memberId)
            .activityGroup(activityGroup)
            .isDeleted(false)
            .build();
    }

    public ScheduleResponseDto toDto(Schedule schedule) {
        return ScheduleResponseDto.builder()
            .id(schedule.getId())
            .title(schedule.getTitle())
            .detail(schedule.getDetail())
            .activityName(schedule.isAllSchedule() ? null : schedule.getActivityGroup().getName())
            .startDateTime(schedule.getStartDateTime())
            .endDateTime(schedule.getEndDateTime())
            .priority(schedule.getPriority())
            .build();
    }

    public ScheduleCollectResponseDto of(Long totalScheduleCount, Long totalEventCount) {
        return ScheduleCollectResponseDto.builder()
            .totalScheduleCount(totalScheduleCount)
            .totalEventCount(totalEventCount)
            .build();
    }
}
