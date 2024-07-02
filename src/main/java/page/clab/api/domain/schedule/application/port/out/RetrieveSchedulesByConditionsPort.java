package page.clab.api.domain.schedule.application.port.out;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import page.clab.api.domain.schedule.domain.Schedule;
import page.clab.api.domain.schedule.domain.SchedulePriority;

public interface RetrieveSchedulesByConditionsPort {
    Page<Schedule> findByConditions(Integer year, Integer month, SchedulePriority priority, Pageable pageable);
}
