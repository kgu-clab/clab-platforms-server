package page.clab.api.domain.schedule.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.schedule.application.port.in.RetrieveCollectSchedulesUseCase;
import page.clab.api.domain.schedule.application.port.out.RetrieveSchedulePort;
import page.clab.api.domain.schedule.dto.response.ScheduleCollectResponseDto;

@Service
@RequiredArgsConstructor
public class CollectSchedulesRetrievalService implements RetrieveCollectSchedulesUseCase {

    private final RetrieveSchedulePort retrieveSchedulePort;

    @Override
    @Transactional(readOnly = true)
    public ScheduleCollectResponseDto retrieve() {
        return retrieveSchedulePort.findCollectSchedules();
    }
}
