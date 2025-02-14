package page.clab.api.domain.members.schedule.application.port.in;

import java.time.LocalDate;
import org.springframework.data.domain.Pageable;
import page.clab.api.domain.members.schedule.application.dto.response.ScheduleResponseDto;
import page.clab.api.global.common.dto.PagedResponseDto;

public interface RetrieveSchedulesWithinDateRangeUseCase {

    PagedResponseDto<ScheduleResponseDto> retrieveSchedulesWithinDateRange(LocalDate startDate, LocalDate endDate,
        Pageable pageable);
}
