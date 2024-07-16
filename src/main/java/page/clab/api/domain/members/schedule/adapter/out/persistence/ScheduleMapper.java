package page.clab.api.domain.members.schedule.adapter.out.persistence;

import org.mapstruct.Mapper;
import page.clab.api.domain.members.schedule.domain.Schedule;

@Mapper(componentModel = "spring")
public interface ScheduleMapper {

    ScheduleJpaEntity toJpaEntity(Schedule schedule);

    Schedule toDomainEntity(ScheduleJpaEntity entity);
}
