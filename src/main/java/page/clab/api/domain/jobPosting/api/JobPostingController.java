package page.clab.api.domain.jobPosting.api;

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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.domain.jobPosting.application.JobPostingService;
import page.clab.api.domain.jobPosting.domain.CareerLevel;
import page.clab.api.domain.jobPosting.domain.EmploymentType;
import page.clab.api.domain.jobPosting.dto.request.JobPostingRequestDto;
import page.clab.api.domain.jobPosting.dto.request.JobPostingUpdateRequestDto;
import page.clab.api.domain.jobPosting.dto.response.JobPostingDetailsResponseDto;
import page.clab.api.domain.jobPosting.dto.response.JobPostingResponseDto;
import page.clab.api.global.common.dto.PagedResponseDto;
import page.clab.api.global.common.dto.ApiResponse;

@RestController
@RequestMapping("/api/v1/job-postings")
@RequiredArgsConstructor
@Tag(name = "JobPosting", description = "채용 공고")
@Slf4j
public class JobPostingController {

    private final JobPostingService jobPostingService;

    @Operation(summary = "[A] 채용 공고 등록", description = "ROLE_ADMIN 이상의 권한이 필요함")
    @Secured({"ROLE_ADMIN", "ROLE_SUPER"})
    @PostMapping("")
    public ApiResponse<Long> createJobPosting(
            @Valid @RequestBody JobPostingRequestDto requestDto
    ) {
        Long id = jobPostingService.createJobPosting(requestDto);
        return ApiResponse.success(id);
    }

    @Operation(summary = "[U] 채용 공고 목록 조회(공고명, 기업명, 경력, 근로 조건 기준)", description = "ROLE_USER 이상의 권한이 필요함<br>" +
            "4개의 파라미터를 자유롭게 조합하여 필터링 가능<br>" +
            "공고명, 기업명, 경력, 근로 조건 중 하나라도 입력하지 않으면 전체 조회됨")
    @Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_SUPER"})
    @GetMapping("")
    public ApiResponse<PagedResponseDto<JobPostingResponseDto>> getJobPostingsByConditions(
            @RequestParam(name = "title", required = false) String title,
            @RequestParam(name = "companyName", required = false) String companyName,
            @RequestParam(name = "careerLevel", required = false) CareerLevel careerLevel,
            @RequestParam(name = "employmentType", required = false) EmploymentType employmentType,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        PagedResponseDto<JobPostingResponseDto> jobPostings = jobPostingService.getJobPostingsByConditions(title, companyName, careerLevel, employmentType, pageable);
        return ApiResponse.success(jobPostings);
    }

    @Operation(summary = "[U] 채용 공고 상세 조회", description = "ROLE_USER 이상의 권한이 필요함")
    @Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_SUPER"})
    @GetMapping("/{jobPostingId}")
    public ApiResponse<JobPostingDetailsResponseDto> getJobPosting(
            @PathVariable(name = "jobPostingId") Long jobPostingId
    ) {
        JobPostingDetailsResponseDto jobPosting = jobPostingService.getJobPosting(jobPostingId);
        return ApiResponse.success(jobPosting);
    }

    @Operation(summary = "[A] 채용 공고 수정", description = "ROLE_ADMIN 이상의 권한이 필요함")
    @Secured({"ROLE_ADMIN", "ROLE_SUPER"})
    @PostMapping("/{jobPostingId}")
    public ApiResponse<Long> updateJobPosting(
            @PathVariable(name = "jobPostingId") Long jobPostingId,
            @Valid @RequestBody JobPostingUpdateRequestDto requestDto
    ) {
        Long id = jobPostingService.updateJobPosting(jobPostingId, requestDto);
        return ApiResponse.success(id);
    }

    @Operation(summary = "[A] 채용 공고 삭제", description = "ROLE_ADMIN 이상의 권한이 필요함")
    @Secured({"ROLE_ADMIN", "ROLE_SUPER"})
    @DeleteMapping("/{jobPostingId}")
    public ApiResponse<Long> deleteJobPosting(
            @PathVariable(name = "jobPostingId") Long jobPostingId
    ) {
        Long id = jobPostingService.deleteJobPosting(jobPostingId);
        return ApiResponse.success(id);
    }

}
