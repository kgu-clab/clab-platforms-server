package page.clab.api.domain.members.schedule.application.port.in;

import org.springframework.data.domain.Pageable;
import page.clab.api.domain.members.schedule.application.dto.response.ScheduleResponseDto;
import page.clab.api.domain.members.schedule.domain.SchedulePriority;
import page.clab.api.global.common.dto.PagedResponseDto;

public interface RetrieveSchedulesByConditionsUseCase {
    PagedResponseDto<ScheduleResponseDto> retrieveSchedules(Integer year, Integer month, SchedulePriority priority, Pageable pageable);
}
