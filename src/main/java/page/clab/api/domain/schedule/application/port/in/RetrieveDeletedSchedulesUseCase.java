package page.clab.api.domain.schedule.application.port.in;

import org.springframework.data.domain.Pageable;
import page.clab.api.domain.schedule.dto.response.ScheduleResponseDto;
import page.clab.api.global.common.dto.PagedResponseDto;

public interface RetrieveDeletedSchedulesUseCase {
    PagedResponseDto<ScheduleResponseDto> retrieve(Pageable pageable);
}
