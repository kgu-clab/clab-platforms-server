package page.clab.api.domain.application.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
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
import page.clab.api.domain.application.application.ApplicationService;
import page.clab.api.domain.application.dto.request.ApplicationRequestDto;
import page.clab.api.domain.application.dto.response.ApplicationPassResponseDto;
import page.clab.api.domain.application.dto.response.ApplicationResponseDto;
import page.clab.api.domain.board.dto.response.BoardListResponseDto;
import page.clab.api.global.common.dto.PagedResponseDto;
import page.clab.api.global.common.dto.ApiResponse;

@RestController
@RequestMapping("/api/v1/applications")
@RequiredArgsConstructor
@Tag(name = "Application", description = "동아리 지원")
@Slf4j
public class ApplicationController {

    private final ApplicationService applicationService;

    @Operation(summary = "동아리 지원", description = "ROLE_ANONYMOUS 이상의 권한이 필요함")
    @PostMapping("")
    public ApiResponse<String> createApplication(
            HttpServletRequest request,
            @Valid @RequestBody ApplicationRequestDto requestDto
    ) {
        String id = applicationService.createApplication(request, requestDto);
        return ApiResponse.success(id);
    }

    @Operation(summary = "[A] 지원자 목록 조회(모집 일정 ID, 지원자 ID, 합격 여부 기준)", description = "ROLE_ADMIN 이상의 권한이 필요함<br>" +
            "3개의 파라미터를 자유롭게 조합하여 필터링 가능<br>" +
            "모집 일정 ID, 지원자 ID, 합격 여부 중 하나라도 입력하지 않으면 전체 조회됨")
    @Secured({"ROLE_ADMIN", "ROLE_SUPER"})
    @GetMapping("/conditions")
    public ApiResponse<PagedResponseDto<ApplicationResponseDto>> getApplicationsByConditions(
            @RequestParam(name = "recruitmentId", required = false) Long recruitmentId,
            @RequestParam(name = "studentId", required = false) String studentId,
            @RequestParam(name = "isPass", required = false) Boolean isPass,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        PagedResponseDto<ApplicationResponseDto> applications = applicationService.getApplicationsByConditions(recruitmentId, studentId, isPass, pageable);
        return ApiResponse.success(applications);
    }

    @Operation(summary = "[S] 지원 합격/취소", description = "ROLE_SUPER 이상의 권한이 필요함<br>" +
            "승인/취소 상태가 반전됨")
    @Secured({"ROLE_SUPER"})
    @PatchMapping("/{recruitmentId}/{studentId}")
    public ApiResponse<String> toggleApprovalStatus(
            @PathVariable(name = "recruitmentId") Long recruitmentId,
            @PathVariable(name = "studentId") String studentId
    ) {
        String id = applicationService.toggleApprovalStatus(recruitmentId, studentId);
        return ApiResponse.success(id);
    }

    @Operation(summary = "합격 여부 조회", description = "ROLE_ANONYMOUS 이상의 권한이 필요함")
    @GetMapping("/{recruitmentId}/{studentId}")
    public ApiResponse<ApplicationPassResponseDto> getApplicationPass(
            @PathVariable(name = "recruitmentId") Long recruitmentId,
            @PathVariable(name = "studentId") String studentId
    ) {
        ApplicationPassResponseDto pass = applicationService.getApplicationPass(recruitmentId, studentId);
        return ApiResponse.success(pass);
    }

    @Operation(summary = "[S] 지원서 삭제", description = "ROLE_ADMIN 이상의 권한이 필요함")
    @Secured({"ROLE_SUPER"})
    @DeleteMapping("/{recruitmentId}/{studentId}")
    public ApiResponse<String> deleteApplication(
            @PathVariable(name = "recruitmentId") Long recruitmentId,
            @PathVariable(name = "studentId") String studentId
    ) {
        String id = applicationService.deleteApplication(recruitmentId, studentId);
        return ApiResponse.success(id);
    }

    @GetMapping("/deleted")
    @Operation(summary = "[S] 삭제된 지원서 조회하기", description = "ROLE_SUPER 이상의 권한이 필요함")
    @Secured({"ROLE_SUPER"})
    public ApiResponse<PagedResponseDto<ApplicationResponseDto>> getDeletedBoards(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        PagedResponseDto<ApplicationResponseDto> applications = applicationService.getDeletedApplications(pageable);
        return ApiResponse.success(applications);
    }

}
