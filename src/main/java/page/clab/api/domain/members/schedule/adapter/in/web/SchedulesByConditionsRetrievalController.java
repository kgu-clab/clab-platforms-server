package page.clab.api.domain.members.schedule.adapter.in.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.domain.members.schedule.application.dto.response.ScheduleResponseDto;
import page.clab.api.domain.members.schedule.application.port.in.RetrieveSchedulesByConditionsUseCase;
import page.clab.api.domain.members.schedule.domain.SchedulePriority;
import page.clab.api.global.common.dto.ApiResponse;
import page.clab.api.global.common.dto.PagedResponseDto;
import page.clab.api.global.util.PageableUtils;

@RestController
@RequestMapping("/api/v1/schedules")
@RequiredArgsConstructor
@Tag(name = "Members - Schedule", description = "일정")
public class SchedulesByConditionsRetrievalController {

    private final RetrieveSchedulesByConditionsUseCase retrieveSchedulesByConditionsUseCase;
    private final PageableUtils pageableUtils;

    @Operation(summary = "[G] 일정 조회(연도, 월, 중요도 기준)", description = "ROLE_GUEST 이상의 권한이 필요함<br>" +
        "3개의 파라미터를 자유롭게 조합하여 필터링 가능<br>" +
        "연도, 월, 중요도 중 하나라도 입력하지 않으면 전체 조회됨<br>" +
        "DTO의 필드명을 기준으로 정렬 가능하며, 정렬 방향은 오름차순(asc)과 내림차순(desc)이 가능함")
    @PreAuthorize("hasRole('GUEST')")
    @GetMapping("/conditions")
    public ApiResponse<PagedResponseDto<ScheduleResponseDto>> retrieveSchedulesByConditions(
        @RequestParam(name = "year", required = false) Integer year,
        @RequestParam(name = "month", required = false) Integer month,
        @RequestParam(name = "priority", required = false) SchedulePriority priority,
        @RequestParam(name = "page", defaultValue = "0") int page,
        @RequestParam(name = "size", defaultValue = "20") int size,
        @RequestParam(name = "sortBy", defaultValue = "startDateTime") List<String> sortBy,
        @RequestParam(name = "sortDirection", defaultValue = "asc") List<String> sortDirection
    ) {
        Pageable pageable = pageableUtils.createPageable(page, size, sortBy, sortDirection, ScheduleResponseDto.class);
        PagedResponseDto<ScheduleResponseDto> schedules =
            retrieveSchedulesByConditionsUseCase.retrieveSchedules(year, month, priority, pageable);
        return ApiResponse.success(schedules);
    }
}
