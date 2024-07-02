package page.clab.api.domain.schedule.application.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.schedule.application.CollectSchedulesRetrievalUseCase;
import page.clab.api.domain.schedule.dao.ScheduleRepository;
import page.clab.api.domain.schedule.dto.response.ScheduleCollectResponseDto;

@Service
@RequiredArgsConstructor
public class CollectSchedulesRetrievalService implements CollectSchedulesRetrievalUseCase {

    private final ScheduleRepository scheduleRepository;

    @Override
    @Transactional(readOnly = true)
    public ScheduleCollectResponseDto retrieve() {
        return scheduleRepository.findCollectSchedules();
    }
}
