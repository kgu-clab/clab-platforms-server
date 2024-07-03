package page.clab.api.domain.schedule.application.port.in;

import org.springframework.data.domain.Pageable;
import page.clab.api.domain.schedule.domain.SchedulePriority;
import page.clab.api.domain.schedule.dto.response.ScheduleResponseDto;
import page.clab.api.global.common.dto.PagedResponseDto;

public interface RetrieveSchedulesByConditionsUseCase {
    PagedResponseDto<ScheduleResponseDto> retrieve(Integer year, Integer month, SchedulePriority priority, Pageable pageable);
}
