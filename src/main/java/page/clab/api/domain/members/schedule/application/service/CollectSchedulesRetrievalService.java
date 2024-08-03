package page.clab.api.domain.members.schedule.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.members.schedule.application.dto.response.ScheduleCollectResponseDto;
import page.clab.api.domain.members.schedule.application.port.in.RetrieveCollectSchedulesUseCase;
import page.clab.api.domain.members.schedule.application.port.out.RetrieveSchedulePort;

@Service
@RequiredArgsConstructor
public class CollectSchedulesRetrievalService implements RetrieveCollectSchedulesUseCase {

    private final RetrieveSchedulePort retrieveSchedulePort;

    @Override
    @Transactional(readOnly = true)
    public ScheduleCollectResponseDto retrieveCollectSchedules() {
        return retrieveSchedulePort.findCollectSchedules();
    }
}
