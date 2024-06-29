package page.clab.api.domain.workExperience.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.domain.workExperience.application.WorkExperiencesByConditionsRetrievalService;
import page.clab.api.domain.workExperience.domain.WorkExperience;
import page.clab.api.domain.workExperience.dto.response.WorkExperienceResponseDto;
import page.clab.api.global.common.dto.ApiResponse;
import page.clab.api.global.common.dto.PagedResponseDto;
import page.clab.api.global.exception.InvalidColumnException;
import page.clab.api.global.exception.SortingArgumentException;
import page.clab.api.global.util.PageableUtils;

import java.util.List;

@RestController
@RequestMapping("/api/v1/work-experiences")
@RequiredArgsConstructor
@Tag(name = "WorkExperience", description = "경력사항")
public class WorkExperiencesByConditionsRetrievalController {

    private final WorkExperiencesByConditionsRetrievalService workExperiencesByConditionsRetrievalService;

    @Operation(summary = "[U] 멤버의 경력사항 검색", description = "ROLE_USER 이상의 권한이 필요함<br>" +
            "입사일을 기준으로 내림차순 정렬하여 결과를 보여줌<br>" +
            "페이지네이션 정렬에 사용할 수 있는 칼럼 : createdAt, id, updatedAt, endDate, startDate, memberId")
    @Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_SUPER"})
    @GetMapping("/conditions")
    public ApiResponse<PagedResponseDto<WorkExperienceResponseDto>> retrieveWorkExperiencesByConditions(
            @RequestParam String memberId,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size,
            @RequestParam(name = "sortBy", defaultValue = "startDate") List<String> sortBy,
            @RequestParam(name = "sortDirection", defaultValue = "desc") List<String> sortDirection
    ) throws SortingArgumentException, InvalidColumnException {
        Pageable pageable = PageableUtils.createPageable(page, size, sortBy, sortDirection, WorkExperience.class);
        PagedResponseDto<WorkExperienceResponseDto> workExperiences = workExperiencesByConditionsRetrievalService.retrieveByConditions(memberId, pageable);
        return ApiResponse.success(workExperiences);
    }
}
