package page.clab.api.domain.schedule.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.domain.schedule.application.ScheduleRegisterService;
import page.clab.api.domain.schedule.dto.request.ScheduleRequestDto;
import page.clab.api.global.common.dto.ApiResponse;
import page.clab.api.global.exception.PermissionDeniedException;

@RestController
@RequestMapping("/api/v1/schedules")
@RequiredArgsConstructor
@Tag(name = "Schedule", description = "일정")
public class ScheduleRegisterController {

    private final ScheduleRegisterService scheduleRegisterService;

    @Operation(summary = "[U] 일정 등록", description = "ROLE_USER 이상의 권한이 필요함")
    @Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_SUPER"})
    @PostMapping("")
    public ApiResponse<Long> registerSchedule(
            @Valid @RequestBody ScheduleRequestDto requestDto
    ) throws PermissionDeniedException {
        Long id = scheduleRegisterService.register(requestDto);
        return ApiResponse.success(id);
    }
}
