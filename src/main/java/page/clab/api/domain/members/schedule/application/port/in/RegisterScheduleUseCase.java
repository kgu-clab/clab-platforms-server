package page.clab.api.domain.members.schedule.application.port.in;

import page.clab.api.domain.members.schedule.application.dto.request.ScheduleRequestDto;

public interface RegisterScheduleUseCase {

    Long registerSchedule(ScheduleRequestDto requestDto);
}
