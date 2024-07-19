package page.clab.api.domain.members.schedule.application.port.in;

import page.clab.api.domain.members.schedule.application.dto.response.ScheduleCollectResponseDto;

public interface RetrieveCollectSchedulesUseCase {
    ScheduleCollectResponseDto retrieveCollectSchedules();
}
