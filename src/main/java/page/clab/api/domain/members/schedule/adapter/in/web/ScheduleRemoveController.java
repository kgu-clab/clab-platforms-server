package page.clab.api.domain.members.schedule.adapter.in.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.domain.members.schedule.application.port.in.RemoveScheduleUseCase;
import page.clab.api.global.common.dto.ApiResponse;

@RestController
@RequestMapping("/api/v1/schedules")
@RequiredArgsConstructor
@Tag(name = "Members - Schedule", description = "일정")
public class ScheduleRemoveController {

    private final RemoveScheduleUseCase removeScheduleUseCase;

    @Operation(summary = "[U] 일정 삭제", description = "ROLE_USER 이상의 권한이 필요함")
    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("/{scheduleId}")
    public ApiResponse<Long> removeSchedule(
        @PathVariable(name = "scheduleId") Long scheduleId
    ) {
        Long id = removeScheduleUseCase.removeSchedule(scheduleId);
        return ApiResponse.success(id);
    }
}
