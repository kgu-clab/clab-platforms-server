package page.clab.api.domain.members.schedule.adapter.in.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.domain.members.schedule.application.dto.request.ScheduleRequestDto;
import page.clab.api.domain.members.schedule.application.port.in.RegisterScheduleUseCase;
import page.clab.api.global.common.dto.ApiResponse;
import page.clab.api.global.exception.PermissionDeniedException;

@RestController
@RequestMapping("/api/v1/schedules")
@RequiredArgsConstructor
@Tag(name = "Members - Schedule", description = "일정")
public class ScheduleRegisterController {

    private final RegisterScheduleUseCase registerScheduleUseCase;

    @Operation(summary = "[U] 일정 등록", description = "ROLE_USER 이상의 권한이 필요함")
    @PreAuthorize("hasRole('USER')")
    @PostMapping("")
    public ApiResponse<Long> registerSchedule(
        @Valid @RequestBody ScheduleRequestDto requestDto
    ) throws PermissionDeniedException {
        Long id = registerScheduleUseCase.registerSchedule(requestDto);
        return ApiResponse.success(id);
    }
}
