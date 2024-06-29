package page.clab.api.domain.schedule.application;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.schedule.dao.ScheduleRepository;
import page.clab.api.domain.schedule.domain.Schedule;
import page.clab.api.domain.schedule.dto.response.ScheduleResponseDto;
import page.clab.api.global.common.dto.PagedResponseDto;

@Service
@RequiredArgsConstructor
public class FetchDeletedSchedulesServiceImpl implements FetchDeletedSchedulesService {

    private final ScheduleRepository scheduleRepository;

    @Override
    @Transactional(readOnly = true)
    public PagedResponseDto<ScheduleResponseDto> execute(Pageable pageable) {
        Page<Schedule> schedules = scheduleRepository.findAllByIsDeletedTrue(pageable);
        return new PagedResponseDto<>(schedules.map(ScheduleResponseDto::toDto));
    }
}
