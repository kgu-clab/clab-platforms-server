package page.clab.api.domain.schedule.application.port.out;

import page.clab.api.domain.schedule.dto.response.ScheduleCollectResponseDto;

public interface RetrieveCollectSchedulesPort {
    ScheduleCollectResponseDto findCollectSchedules();
}
