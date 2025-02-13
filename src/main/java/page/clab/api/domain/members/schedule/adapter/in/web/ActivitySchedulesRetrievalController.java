package page.clab.api.domain.members.schedule.adapter.in.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.domain.members.schedule.application.dto.response.ScheduleResponseDto;
import page.clab.api.domain.members.schedule.application.port.in.RetrieveActivitySchedulesUseCase;
import page.clab.api.global.common.dto.ApiResponse;
import page.clab.api.global.common.dto.PagedResponseDto;
import page.clab.api.global.exception.SortingArgumentException;
import page.clab.api.global.util.PageableUtils;

@RestController
@RequestMapping("/api/v1/schedules")
@RequiredArgsConstructor
@Tag(name = "Members - Schedule", description = "일정")
public class ActivitySchedulesRetrievalController {

    private final RetrieveActivitySchedulesUseCase retrieveActivitySchedulesUseCase;
    private final PageableUtils pageableUtils;

    @Operation(summary = "[G] 내 활동 일정 조회", description = "ROLE_GUEST 이상의 권한이 필요함<br>" +
        "DTO의 필드명을 기준으로 정렬 가능하며, 정렬 방향은 오름차순(asc)과 내림차순(desc)이 가능함")
    @PreAuthorize("hasRole('GUEST')")
    @GetMapping("/activity")
    public ApiResponse<PagedResponseDto<ScheduleResponseDto>> retrieveActivitySchedules(
        @RequestParam(name = "startDate") LocalDate startDate,
        @RequestParam(name = "endDate") LocalDate endDate,
        @RequestParam(name = "page", defaultValue = "0") int page,
        @RequestParam(name = "size", defaultValue = "20") int size,
        @RequestParam(name = "sortBy", defaultValue = "startDateTime") List<String> sortBy,
        @RequestParam(name = "sortDirection", defaultValue = "asc") List<String> sortDirection
    ) throws SortingArgumentException {
        Pageable pageable = pageableUtils.createPageable(page, size, sortBy, sortDirection, ScheduleResponseDto.class);
        PagedResponseDto<ScheduleResponseDto> schedules =
            retrieveActivitySchedulesUseCase.retrieveActivitySchedules(startDate, endDate, pageable);
        return ApiResponse.success(schedules);
    }
}
