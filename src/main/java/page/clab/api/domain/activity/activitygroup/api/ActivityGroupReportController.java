package page.clab.api.domain.activity.activitygroup.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.domain.activity.activitygroup.application.ActivityGroupReportService;
import page.clab.api.domain.activity.activitygroup.dto.request.ActivityGroupReportRequestDto;
import page.clab.api.domain.activity.activitygroup.dto.request.ActivityGroupReportUpdateRequestDto;
import page.clab.api.domain.activity.activitygroup.dto.response.ActivityGroupReportResponseDto;
import page.clab.api.global.common.dto.ApiResponse;
import page.clab.api.global.common.dto.PagedResponseDto;
import page.clab.api.global.util.PageableUtils;

@RestController
@RequestMapping("/api/v1/activity-group/report")
@RequiredArgsConstructor
@Tag(name = "Activity - Group Report", description = "활동 그룹 보고서")
public class ActivityGroupReportController {

    private final ActivityGroupReportService activityGroupReportService;
    private final PageableUtils pageableUtils;

    @Operation(summary = "[U] 활동 보고서 작성", description = "ROLE_USER 이상의 권한이 필요함")
    @PreAuthorize("hasRole('USER')")
    @PostMapping("")
    public ApiResponse<Long> writeReport(
        @Valid @RequestBody ActivityGroupReportRequestDto requestDto
    ) throws IllegalAccessException {
        Long id = activityGroupReportService.writeReport(requestDto);
        return ApiResponse.success(id);
    }

    @Operation(summary = "[U] 특정 그룹의 활동 보고서 전체 조회", description = "ROLE_USER 이상의 권한이 필요함<br>" +
        "DTO의 필드명을 기준으로 정렬 가능하며, 정렬 방향은 오름차순(asc)과 내림차순(desc)이 가능함")
    @PreAuthorize("hasRole('USER')")
    @GetMapping("")
    public ApiResponse<PagedResponseDto<ActivityGroupReportResponseDto>> getReports(
        @RequestParam(name = "activityGroupId") Long activityGroupId,
        @RequestParam(name = "page", defaultValue = "0") int page,
        @RequestParam(name = "size", defaultValue = "20") int size,
        @RequestParam(name = "sortBy", defaultValue = "createdAt") List<String> sortBy,
        @RequestParam(name = "sortDirection", defaultValue = "desc") List<String> sortDirection
    ) {
        Pageable pageable = pageableUtils.createPageable(page, size, sortBy, sortDirection,
            ActivityGroupReportResponseDto.class);
        PagedResponseDto<ActivityGroupReportResponseDto> reports = activityGroupReportService.getReports(
            activityGroupId, pageable);
        return ApiResponse.success(reports);
    }

    @Operation(summary = "[U] 특정 그룹의 특정 차시 활동 보고서 검색", description = "ROLE_USER 이상의 권한이 필요함")
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/search")
    public ApiResponse<ActivityGroupReportResponseDto> searchReport(
        @RequestParam(name = "activityGroupId") Long activityGroupId,
        @RequestParam(name = "turn") Long turn
    ) {
        ActivityGroupReportResponseDto report = activityGroupReportService.searchReport(activityGroupId, turn);
        return ApiResponse.success(report);
    }

    @Operation(summary = "[U] 활동 보고서 수정", description = "ROLE_USER 이상의 권한이 필요함")
    @PreAuthorize("hasRole('USER')")
    @PatchMapping("/{reportId}")
    public ApiResponse<Long> updateReport(
        @PathVariable(name = "reportId") Long reportId,
        @RequestParam(name = "activityGroupId") Long activityGroupId,
        @Valid @RequestBody ActivityGroupReportUpdateRequestDto requestDto
    ) throws IllegalAccessException {
        Long id = activityGroupReportService.updateReport(reportId, activityGroupId, requestDto);
        return ApiResponse.success(id);
    }

    @Operation(summary = "[U] 활동보고서 삭제", description = "ROLE_USER 이상의 권한이 필요함")
    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("/{reportId}")
    public ApiResponse<Long> deleteAward(
        @PathVariable(name = "reportId") Long reportId
    ) {
        Long id = activityGroupReportService.deleteReport(reportId);
        return ApiResponse.success(id);
    }


    @Operation(summary = "[S] 삭제된 활동보고서 조회하기", description = "ROLE_SUPER 이상의 권한이 필요함")
    @PreAuthorize("hasRole('SUPER')")
    @GetMapping("/deleted")
    public ApiResponse<PagedResponseDto<ActivityGroupReportResponseDto>> getDeletedActivityGroupReports(
        @RequestParam(name = "page", defaultValue = "0") int page,
        @RequestParam(name = "size", defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        PagedResponseDto<ActivityGroupReportResponseDto> activityGroupReports = activityGroupReportService.getDeletedActivityGroupReports(
            pageable);
        return ApiResponse.success(activityGroupReports);
    }
}
