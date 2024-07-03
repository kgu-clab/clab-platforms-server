package page.clab.api.domain.schedule.application;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.member.application.port.in.MemberRetrievalUseCase;
import page.clab.api.domain.member.domain.Member;
import page.clab.api.domain.schedule.application.port.in.ActivitySchedulesRetrievalUseCase;
import page.clab.api.domain.schedule.application.port.out.RetrieveActivitySchedulesPort;
import page.clab.api.domain.schedule.domain.Schedule;
import page.clab.api.domain.schedule.dto.response.ScheduleResponseDto;
import page.clab.api.global.common.dto.PagedResponseDto;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class ActivitySchedulesRetrievalService implements ActivitySchedulesRetrievalUseCase {

    private final MemberRetrievalUseCase memberRetrievalUseCase;
    private final RetrieveActivitySchedulesPort retrieveActivitySchedulesPort;

    @Override
    @Transactional(readOnly = true)
    public PagedResponseDto<ScheduleResponseDto> retrieve(LocalDate startDate, LocalDate endDate, Pageable pageable) {
        Member currentMember = memberRetrievalUseCase.getCurrentMember();
        Page<Schedule> schedules = retrieveActivitySchedulesPort.findActivitySchedulesByDateRangeAndMember(startDate, endDate, currentMember, pageable);
        return new PagedResponseDto<>(schedules.map(ScheduleResponseDto::toDto));
    }
}
