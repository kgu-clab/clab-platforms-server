package page.clab.api.domain.members.schedule.application.port.in;

import page.clab.api.domain.members.schedule.application.dto.request.ScheduleRequestDto;
import page.clab.api.global.exception.PermissionDeniedException;

public interface RegisterScheduleUseCase {
    Long registerSchedule(ScheduleRequestDto requestDto) throws PermissionDeniedException;
}
