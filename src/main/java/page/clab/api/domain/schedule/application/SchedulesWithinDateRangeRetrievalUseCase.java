package page.clab.api.domain.schedule.application;

import org.springframework.data.domain.Pageable;
import page.clab.api.domain.schedule.dto.response.ScheduleResponseDto;
import page.clab.api.global.common.dto.PagedResponseDto;

import java.time.LocalDate;

public interface SchedulesWithinDateRangeRetrievalUseCase {
    PagedResponseDto<ScheduleResponseDto> retrieve(LocalDate startDate, LocalDate endDate, Pageable pageable);
}
