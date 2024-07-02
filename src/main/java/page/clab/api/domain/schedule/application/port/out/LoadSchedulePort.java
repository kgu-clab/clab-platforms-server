package page.clab.api.domain.schedule.application.port.out;

import page.clab.api.domain.schedule.domain.Schedule;

import java.util.Optional;

public interface LoadSchedulePort {
    Optional<Schedule> findById(Long id);
    Schedule findByIdOrThrow(Long id);
}
