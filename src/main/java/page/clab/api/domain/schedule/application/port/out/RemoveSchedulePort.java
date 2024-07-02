package page.clab.api.domain.schedule.application.port.out;

import page.clab.api.domain.schedule.domain.Schedule;

public interface RemoveSchedulePort {
    Schedule findScheduleByIdOrThrow(Long id);
    Schedule save(Schedule schedule);
}