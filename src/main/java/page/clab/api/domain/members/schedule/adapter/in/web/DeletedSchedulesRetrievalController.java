package page.clab.api.domain.members.schedule.adapter.in.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.domain.members.schedule.application.dto.response.ScheduleResponseDto;
import page.clab.api.domain.members.schedule.application.port.in.RetrieveDeletedSchedulesUseCase;
import page.clab.api.global.common.dto.ApiResponse;
import page.clab.api.global.common.dto.PagedResponseDto;

@RestController
@RequestMapping("/api/v1/schedules")
@RequiredArgsConstructor
@Tag(name = "Members - Schedule", description = "일정")
public class DeletedSchedulesRetrievalController {

    private final RetrieveDeletedSchedulesUseCase retrieveDeletedSchedulesUseCase;

    @Operation(summary = "[S] 삭제된 일정 조회하기", description = "ROLE_SUPER 이상의 권한이 필요함")
    @Secured({ "ROLE_SUPER" })
    @GetMapping("/deleted")
    public ApiResponse<PagedResponseDto<ScheduleResponseDto>> retrieveDeletedSchedules(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        PagedResponseDto<ScheduleResponseDto> schedules = retrieveDeletedSchedulesUseCase.retrieveDeletedSchedules(pageable);
        return ApiResponse.success(schedules);
    }
}
