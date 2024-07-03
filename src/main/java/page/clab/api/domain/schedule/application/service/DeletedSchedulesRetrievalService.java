package page.clab.api.domain.schedule.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.schedule.application.port.in.RetrieveDeletedSchedulesUseCase;
import page.clab.api.domain.schedule.application.port.out.RetrieveDeletedSchedulesPort;
import page.clab.api.domain.schedule.domain.Schedule;
import page.clab.api.domain.schedule.dto.response.ScheduleResponseDto;
import page.clab.api.global.common.dto.PagedResponseDto;

@Service
@RequiredArgsConstructor
public class DeletedSchedulesRetrievalService implements RetrieveDeletedSchedulesUseCase {

    private final RetrieveDeletedSchedulesPort retrieveDeletedSchedulesPort;

    @Override
    @Transactional(readOnly = true)
    public PagedResponseDto<ScheduleResponseDto> retrieve(Pageable pageable) {
        Page<Schedule> schedules = retrieveDeletedSchedulesPort.findAllByIsDeletedTrue(pageable);
        return new PagedResponseDto<>(schedules.map(ScheduleResponseDto::toDto));
    }
}
