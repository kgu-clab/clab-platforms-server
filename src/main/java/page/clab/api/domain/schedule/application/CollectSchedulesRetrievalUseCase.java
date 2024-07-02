package page.clab.api.domain.schedule.application;

import page.clab.api.domain.schedule.dto.response.ScheduleCollectResponseDto;

public interface CollectSchedulesRetrievalUseCase {
    ScheduleCollectResponseDto retrieve();
}
