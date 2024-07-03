package page.clab.api.domain.schedule.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.domain.schedule.application.port.in.RetrieveCollectSchedulesUseCase;
import page.clab.api.domain.schedule.dto.response.ScheduleCollectResponseDto;
import page.clab.api.global.common.dto.ApiResponse;

@RestController
@RequestMapping("/api/v1/schedules")
@RequiredArgsConstructor
@Tag(name = "Schedule", description = "일정")
public class CollectSchedulesRetrievalController {

    private final RetrieveCollectSchedulesUseCase retrieveCollectSchedulesUseCase;

    @Operation(summary = "[U] 일정 모아보기", description = "ROLE_USER 이상의 권한이 필요함")
    @Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_SUPER"})
    @GetMapping("/collect")
    public ApiResponse<ScheduleCollectResponseDto> retrieveCollectSchedules() {
        ScheduleCollectResponseDto schedules = retrieveCollectSchedulesUseCase.retrieve();
        return ApiResponse.success(schedules);
    }
}
