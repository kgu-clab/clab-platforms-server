package page.clab.api.domain.schedule.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.domain.schedule.application.ScheduleService;
import page.clab.api.domain.schedule.dto.request.ScheduleRequestDto;
import page.clab.api.domain.schedule.dto.response.ScheduleResponseDto;
import page.clab.api.global.common.dto.PagedResponseDto;
import page.clab.api.global.common.dto.ResponseModel;
import page.clab.api.global.exception.PermissionDeniedException;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/v1/schedule")
@RequiredArgsConstructor
@Tag(name = "Schedule", description = "일정")
public class ScheduleController {

    private final ScheduleService scheduleService;

    @Operation(summary = "[U] 일정 등록", description = "ROLE_USER 이상의 권한이 필요함")
    @Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_SUPER"})
    @PostMapping("")
    public ResponseModel<Long> createSchedule(
            @Valid @RequestBody ScheduleRequestDto requestDto
    ) throws PermissionDeniedException {
        Long id = scheduleService.createSchedule(requestDto);
        return ResponseModel.success(id);
    }

    @Operation(summary = "[U] 일정 조회", description = "ROLE_USER 이상의 권한이 필요함")
    @Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_SUPER"})
    @GetMapping("")
    public ResponseModel<PagedResponseDto<ScheduleResponseDto>> getSchedulesWithinDateRange(
            @RequestParam(name = "startDate") LocalDate startDate,
            @RequestParam(name = "endDate") LocalDate endDate,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        PagedResponseDto<ScheduleResponseDto> schedules = scheduleService.getSchedulesWithinDateRange(startDate, endDate, pageable);
        return ResponseModel.success(schedules);
    }

    @Operation(summary = "[U] 내 활동 일정 조회", description = "ROLE_USER 이상의 권한이 필요함")
    @Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_SUPER"})
    @GetMapping("/activity")
    public ResponseModel<PagedResponseDto<ScheduleResponseDto>> getActivitySchedules(
            @RequestParam(name = "startDate") LocalDate startDate,
            @RequestParam(name = "endDate") LocalDate endDate,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        PagedResponseDto<ScheduleResponseDto> schedules = scheduleService.getActivitySchedules(startDate, endDate, pageable);
        return ResponseModel.success(schedules);
    }

    @Operation(summary = "[U] 일정 삭제", description = "ROLE_USER 이상의 권한이 필요함")
    @Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_SUPER"})
    @DeleteMapping("/{scheduleId}")
    public ResponseModel<Long> deleteSchedule(
            @PathVariable(name = "scheduleId") Long scheduleId
    ) throws PermissionDeniedException {
        Long id = scheduleService.deleteSchedule(scheduleId);
        return ResponseModel.success(id);
    }

}
