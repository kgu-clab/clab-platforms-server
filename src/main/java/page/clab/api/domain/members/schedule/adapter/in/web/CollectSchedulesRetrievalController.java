package page.clab.api.domain.members.schedule.adapter.in.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.domain.members.schedule.application.dto.response.ScheduleCollectResponseDto;
import page.clab.api.domain.members.schedule.application.port.in.RetrieveCollectSchedulesUseCase;
import page.clab.api.global.common.dto.ApiResponse;

@RestController
@RequestMapping("/api/v1/schedules")
@RequiredArgsConstructor
@Tag(name = "Members - Schedule", description = "일정")
public class CollectSchedulesRetrievalController {

    private final RetrieveCollectSchedulesUseCase retrieveCollectSchedulesUseCase;

    @Operation(summary = "[G] 일정 모아보기", description = "ROLE_GUEST 이상의 권한이 필요함")
    @PreAuthorize("hasRole('GUEST')")
    @GetMapping("/collect")
    public ApiResponse<ScheduleCollectResponseDto> retrieveCollectSchedules() {
        ScheduleCollectResponseDto schedules = retrieveCollectSchedulesUseCase.retrieveCollectSchedules();
        return ApiResponse.success(schedules);
    }
}
