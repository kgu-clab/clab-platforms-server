package page.clab.api.domain.members.schedule.application.port.in;

import page.clab.api.global.exception.PermissionDeniedException;

public interface RemoveScheduleUseCase {
    Long removeSchedule(Long scheduleId) throws PermissionDeniedException;
}
