package page.clab.api.domain.schedule.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.schedule.application.port.in.CollectSchedulesRetrievalUseCase;
import page.clab.api.domain.schedule.application.port.out.RetrieveCollectSchedulesPort;
import page.clab.api.domain.schedule.dto.response.ScheduleCollectResponseDto;

@Service
@RequiredArgsConstructor
public class CollectSchedulesRetrievalService implements CollectSchedulesRetrievalUseCase {

    private final RetrieveCollectSchedulesPort retrieveCollectSchedulesPort;

    @Override
    @Transactional(readOnly = true)
    public ScheduleCollectResponseDto retrieve() {
        return retrieveCollectSchedulesPort.findCollectSchedules();
    }
}
