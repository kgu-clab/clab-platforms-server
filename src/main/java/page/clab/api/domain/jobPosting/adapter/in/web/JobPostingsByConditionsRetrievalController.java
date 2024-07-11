package page.clab.api.domain.jobPosting.adapter.in.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.domain.jobPosting.application.dto.response.JobPostingResponseDto;
import page.clab.api.domain.jobPosting.application.port.in.RetrieveJobPostingsByConditionsUseCase;
import page.clab.api.domain.jobPosting.domain.CareerLevel;
import page.clab.api.domain.jobPosting.domain.EmploymentType;
import page.clab.api.global.common.dto.ApiResponse;
import page.clab.api.global.common.dto.PagedResponseDto;
import page.clab.api.global.exception.InvalidColumnException;
import page.clab.api.global.exception.SortingArgumentException;
import page.clab.api.global.util.PageableUtils;

import java.util.List;

@RestController
@RequestMapping("/api/v1/job-postings")
@RequiredArgsConstructor
@Tag(name = "JobPosting", description = "채용 공고")
public class JobPostingsByConditionsRetrievalController {

    private final RetrieveJobPostingsByConditionsUseCase retrieveJobPostingsByConditionsUseCase;
    private final PageableUtils pageableUtils;

    @Operation(summary = "[U] 채용 공고 목록 조회(공고명, 기업명, 경력, 근로 조건 기준)", description = "ROLE_USER 이상의 권한이 필요함<br>" +
            "4개의 파라미터를 자유롭게 조합하여 필터링 가능<br>" +
            "공고명, 기업명, 경력, 근로 조건 중 하나라도 입력하지 않으면 전체 조회됨<br>" +
            "DTO의 필드명을 기준으로 정렬 가능하며, 정렬 방향은 오름차순(asc)과 내림차순(desc)이 가능함")
    @Secured({ "ROLE_USER", "ROLE_ADMIN", "ROLE_SUPER" })
    @GetMapping("")
    public ApiResponse<PagedResponseDto<JobPostingResponseDto>> retrieveJobPostingsByConditions(
            @RequestParam(name = "title", required = false) String title,
            @RequestParam(name = "companyName", required = false) String companyName,
            @RequestParam(name = "careerLevel", required = false) CareerLevel careerLevel,
            @RequestParam(name = "employmentType", required = false) EmploymentType employmentType,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size,
            @RequestParam(name = "sortBy", defaultValue = "createdAt") List<String> sortBy,
            @RequestParam(name = "sortDirection", defaultValue = "desc") List<String> sortDirection
    ) throws SortingArgumentException, InvalidColumnException {
        Pageable pageable = pageableUtils.createPageable(page, size, sortBy, sortDirection, JobPostingResponseDto.class);
        PagedResponseDto<JobPostingResponseDto> jobPostings = retrieveJobPostingsByConditionsUseCase.retrieveJobPostings(title, companyName, careerLevel, employmentType, pageable);
        return ApiResponse.success(jobPostings);
    }
}
