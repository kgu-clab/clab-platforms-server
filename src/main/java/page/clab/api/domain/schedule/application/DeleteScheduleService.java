package page.clab.api.domain.schedule.application;

import page.clab.api.global.exception.PermissionDeniedException;

public interface DeleteScheduleService {
    Long execute(Long scheduleId) throws PermissionDeniedException;
}
