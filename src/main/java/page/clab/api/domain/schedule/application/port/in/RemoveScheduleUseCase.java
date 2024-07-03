package page.clab.api.domain.schedule.application.port.in;

import page.clab.api.global.exception.PermissionDeniedException;

public interface RemoveScheduleUseCase {
    Long remove(Long scheduleId) throws PermissionDeniedException;
}
