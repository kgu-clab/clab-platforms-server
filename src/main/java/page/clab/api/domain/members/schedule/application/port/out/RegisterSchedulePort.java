package page.clab.api.domain.members.schedule.application.port.out;

import page.clab.api.domain.members.schedule.domain.Schedule;

public interface RegisterSchedulePort {

    Schedule save(Schedule schedule);
}
