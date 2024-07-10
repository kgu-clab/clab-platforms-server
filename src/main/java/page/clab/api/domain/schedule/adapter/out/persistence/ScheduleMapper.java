package page.clab.api.domain.schedule.adapter.out.persistence;

import org.springframework.stereotype.Component;
import page.clab.api.domain.schedule.domain.Schedule;

@Component
public class ScheduleMapper {

    public ScheduleJpaEntity toJpaEntity(Schedule schedule) {
        return ScheduleJpaEntity.builder()
                .id(schedule.getId())
                .scheduleType(schedule.getScheduleType())
                .title(schedule.getTitle())
                .detail(schedule.getDetail())
                .startDateTime(schedule.getStartDateTime())
                .endDateTime(schedule.getEndDateTime())
                .priority(schedule.getPriority())
                .scheduleWriter(schedule.getScheduleWriter())
                .activityGroup(schedule.getActivityGroup())
                .isDeleted(schedule.isDeleted())
                .build();
    }

    public Schedule toDomainEntity(ScheduleJpaEntity entity) {
        return Schedule.builder()
                .id(entity.getId())
                .scheduleType(entity.getScheduleType())
                .title(entity.getTitle())
                .detail(entity.getDetail())
                .startDateTime(entity.getStartDateTime())
                .endDateTime(entity.getEndDateTime())
                .priority(entity.getPriority())
                .scheduleWriter(entity.getScheduleWriter())
                .activityGroup(entity.getActivityGroup())
                .isDeleted(entity.isDeleted())
                .build();
    }
}
