package page.clab.api.domain.activityGroup.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.domain.activityGroup.application.ActivityGroupReportService;
import page.clab.api.domain.activityGroup.dto.request.ActivityGroupReportRequestDto;
import page.clab.api.domain.activityGroup.dto.request.ActivityGroupReportUpdateRequestDto;
import page.clab.api.domain.activityGroup.dto.response.ActivityGroupReportResponseDto;
import page.clab.api.global.common.dto.PagedResponseDto;
import page.clab.api.global.common.dto.ResponseModel;
import page.clab.api.global.exception.PermissionDeniedException;

@RestController
@RequestMapping("/activity-group/report")
@RequiredArgsConstructor
@Tag(name = "ActivityGroupReport", description = "활동 그룹 보고서 관련 API")
@Slf4j
public class ActivityGroupReportController {

    private final ActivityGroupReportService activityGroupReportService;

    @Operation(summary = "[U] 활동 보고서 작성", description = "ROLE_USER 이상의 권한이 필요함")
    @PostMapping("")
    @Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_SUPER"})
    public ResponseModel writeReport(
            @Valid @RequestBody ActivityGroupReportRequestDto reportRequestDto
    ) throws PermissionDeniedException, IllegalAccessException {
        Long id = activityGroupReportService.writeReport(reportRequestDto);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(id);
        return responseModel;
    }

    @Operation(summary = "[U] 특정 그룹의 활동 보고서 전체 조회", description = "ROLE_USER 이상의 권한이 필요함")
    @GetMapping("")
    @Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_SUPER"})
    public ResponseModel getReports(
            @RequestParam(name = "activityGroupId") Long activityGroupId,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size
    ){
        Pageable pageable = PageRequest.of(page,size);
        PagedResponseDto<ActivityGroupReportResponseDto> reportResponseDtos = activityGroupReportService.getReports(activityGroupId, pageable);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(reportResponseDtos);
        return responseModel;
    }

    @Operation(summary = "[U] 특정 그룹의 특정 차시 활동 보고서 검색", description = "ROLE_USER 이상의 권한이 필요함")
    @GetMapping("/search")
    @Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_SUPER"})
    public ResponseModel searchReport(
            @RequestParam(name = "activityGroupId") Long activityGroupId,
            @RequestParam(name = "turn") Long turn
    ){
        ActivityGroupReportResponseDto report = activityGroupReportService.searchReport(activityGroupId, turn);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(report);
        return responseModel;
    }

    @Operation(summary = "[U] 활동 보고서 수정", description = "ROLE_USER 이상의 권한이 필요함")
    @PatchMapping("/{reportId}")
    @Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_SUPER"})
    public ResponseModel updateReport(
            @PathVariable(name = "reportId") Long reportId,
            @RequestParam(name = "activityGroupId") Long activityGroupId,
            @Valid @RequestBody ActivityGroupReportUpdateRequestDto reportRequestDto
    ) throws PermissionDeniedException, IllegalAccessException {
        Long id = activityGroupReportService.updateReport(reportId, activityGroupId, reportRequestDto);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(id);
        return responseModel;
    }

    @Operation(summary = "[U] 활동보고서 삭제", description = "ROLE_USER 이상의 권한이 필요함")
    @Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_SUPER"})
    @DeleteMapping("/{reportId}")
    public ResponseModel deleteAward(
            @PathVariable(name = "reportId") Long reportId
    ) throws PermissionDeniedException {
        Long id = activityGroupReportService.deleteReport(reportId);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(id);
        return responseModel;
    }

}
