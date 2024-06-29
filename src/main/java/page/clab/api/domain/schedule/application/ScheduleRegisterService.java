package page.clab.api.domain.schedule.application;

import page.clab.api.domain.schedule.dto.request.ScheduleRequestDto;
import page.clab.api.global.exception.PermissionDeniedException;

public interface ScheduleRegisterService {
    Long register(ScheduleRequestDto requestDto) throws PermissionDeniedException;
}
