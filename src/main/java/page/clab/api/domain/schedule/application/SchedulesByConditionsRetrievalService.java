package page.clab.api.domain.schedule.application;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.schedule.application.port.in.SchedulesByConditionsRetrievalUseCase;
import page.clab.api.domain.schedule.application.port.out.RetrieveSchedulesByConditionsPort;
import page.clab.api.domain.schedule.domain.Schedule;
import page.clab.api.domain.schedule.domain.SchedulePriority;
import page.clab.api.domain.schedule.dto.response.ScheduleResponseDto;
import page.clab.api.global.common.dto.PagedResponseDto;

@Service
@RequiredArgsConstructor
public class SchedulesByConditionsRetrievalService implements SchedulesByConditionsRetrievalUseCase {

    private final RetrieveSchedulesByConditionsPort retrieveSchedulesByConditionsPort;

    @Override
    @Transactional(readOnly = true)
    public PagedResponseDto<ScheduleResponseDto> retrieve(Integer year, Integer month, SchedulePriority priority, Pageable pageable) {
        Page<Schedule> schedules = retrieveSchedulesByConditionsPort.findByConditions(year, month, priority, pageable);
        return new PagedResponseDto<>(schedules.map(ScheduleResponseDto::toDto));
    }
}
