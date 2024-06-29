package page.clab.api.domain.application.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.domain.application.application.FetchApplicationsService;
import page.clab.api.domain.application.domain.Application;
import page.clab.api.domain.application.dto.response.ApplicationResponseDto;
import page.clab.api.global.common.dto.ApiResponse;
import page.clab.api.global.common.dto.PagedResponseDto;
import page.clab.api.global.exception.InvalidColumnException;
import page.clab.api.global.exception.SortingArgumentException;
import page.clab.api.global.util.PageableUtils;

import java.util.List;

@RestController
@RequestMapping("/api/v1/applications")
@RequiredArgsConstructor
@Tag(name = "Application", description = "동아리 지원")
public class FetchApplicationsController {

    private final FetchApplicationsService fetchApplicationsService;

    @Operation(summary = "[A] 지원자 목록 조회(모집 일정 ID, 지원자 ID, 합격 여부 기준)", description = "ROLE_ADMIN 이상의 권한이 필요함<br>" +
            "3개의 파라미터를 자유롭게 조합하여 필터링 가능<br>" +
            "모집 일정 ID, 지원자 ID, 합격 여부 중 하나라도 입력하지 않으면 전체 조회됨<br>" +
            "페이지네이션 정렬에 사용할 수 있는 칼럼 : createdAt, id, updatedAt, birth, grade, recruitmentId, studentId")
    @Secured({"ROLE_ADMIN", "ROLE_SUPER"})
    @GetMapping("/conditions")
    public ApiResponse<PagedResponseDto<ApplicationResponseDto>> getApplicationsByConditions(
            @RequestParam(name = "recruitmentId", required = false) Long recruitmentId,
            @RequestParam(name = "studentId", required = false) String studentId,
            @RequestParam(name = "isPass", required = false) Boolean isPass,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size,
            @RequestParam(name = "sortBy", defaultValue = "createdAt") List<String> sortBy,
            @RequestParam(name = "sortDirection", defaultValue = "desc") List<String> sortDirection
    ) throws SortingArgumentException, InvalidColumnException {
        Pageable pageable = PageableUtils.createPageable(page, size, sortBy, sortDirection, Application.class);
        PagedResponseDto<ApplicationResponseDto> applications = fetchApplicationsService.execute(recruitmentId, studentId, isPass, pageable);
        return ApiResponse.success(applications);
    }
}