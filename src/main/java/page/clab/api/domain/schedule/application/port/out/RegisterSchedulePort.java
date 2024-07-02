package page.clab.api.domain.schedule.application.port.out;

import page.clab.api.domain.schedule.domain.Schedule;

public interface RegisterSchedulePort {
    Schedule save(Schedule schedule);
}
