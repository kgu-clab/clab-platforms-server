package page.clab.api.domain.schedule.application;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.member.application.MemberLookupService;
import page.clab.api.domain.member.domain.Member;
import page.clab.api.domain.schedule.dao.ScheduleRepository;
import page.clab.api.domain.schedule.domain.Schedule;
import page.clab.api.domain.schedule.dto.response.ScheduleResponseDto;
import page.clab.api.global.common.dto.PagedResponseDto;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class FetchActivitySchedulesServiceImpl implements FetchActivitySchedulesService {

    private final MemberLookupService memberLookupService;
    private final ScheduleRepository scheduleRepository;

    @Override
    @Transactional(readOnly = true)
    public PagedResponseDto<ScheduleResponseDto> execute(LocalDate startDate, LocalDate endDate, Pageable pageable) {
        Member currentMember = memberLookupService.getCurrentMember();
        Page<Schedule> schedules = scheduleRepository.findActivitySchedulesByDateRangeAndMember(startDate, endDate, currentMember, pageable);
        return new PagedResponseDto<>(schedules.map(ScheduleResponseDto::toDto));
    }
}
