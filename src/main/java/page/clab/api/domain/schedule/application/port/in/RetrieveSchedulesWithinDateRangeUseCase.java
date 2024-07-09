package page.clab.api.domain.schedule.application.port.in;

import org.springframework.data.domain.Pageable;
import page.clab.api.domain.schedule.application.dto.response.ScheduleResponseDto;
import page.clab.api.global.common.dto.PagedResponseDto;

import java.time.LocalDate;

public interface RetrieveSchedulesWithinDateRangeUseCase {
    PagedResponseDto<ScheduleResponseDto> retrieveSchedulesWithinDateRange(LocalDate startDate, LocalDate endDate, Pageable pageable);
}
