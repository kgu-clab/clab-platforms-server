package page.clab.api.domain.schedule.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.member.application.port.in.RetrieveMemberUseCase;
import page.clab.api.domain.schedule.application.dto.response.ScheduleResponseDto;
import page.clab.api.domain.schedule.application.port.in.RetrieveActivitySchedulesUseCase;
import page.clab.api.domain.schedule.application.port.out.RetrieveSchedulePort;
import page.clab.api.domain.schedule.domain.Schedule;
import page.clab.api.global.common.dto.PagedResponseDto;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class ActivitySchedulesRetrievalService implements RetrieveActivitySchedulesUseCase {

    private final RetrieveMemberUseCase retrieveMemberUseCase;
    private final RetrieveSchedulePort retrieveSchedulePort;

    @Override
    @Transactional(readOnly = true)
    public PagedResponseDto<ScheduleResponseDto> retrieveActivitySchedules(LocalDate startDate, LocalDate endDate, Pageable pageable) {
        String currentMemberId = retrieveMemberUseCase.getCurrentMemberId();
        Page<Schedule> schedules = retrieveSchedulePort.findActivitySchedulesByDateRangeAndMemberId(startDate, endDate, currentMemberId, pageable);
        return new PagedResponseDto<>(schedules.map(ScheduleResponseDto::toDto));
    }
}
