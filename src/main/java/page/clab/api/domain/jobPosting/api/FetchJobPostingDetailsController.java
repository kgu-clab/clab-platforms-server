package page.clab.api.domain.jobPosting.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.domain.jobPosting.application.FetchJobPostingDetailsService;
import page.clab.api.domain.jobPosting.dto.response.JobPostingDetailsResponseDto;
import page.clab.api.global.common.dto.ApiResponse;

@RestController
@RequestMapping("/api/v1/job-postings")
@RequiredArgsConstructor
@Tag(name = "JobPosting", description = "채용 공고")
public class FetchJobPostingDetailsController {

    private final FetchJobPostingDetailsService fetchJobPostingDetailsService;

    @Operation(summary = "[U] 채용 공고 상세 조회", description = "ROLE_USER 이상의 권한이 필요함")
    @Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_SUPER"})
    @GetMapping("/{jobPostingId}")
    public ApiResponse<JobPostingDetailsResponseDto> getJobPosting(
            @PathVariable(name = "jobPostingId") Long jobPostingId
    ) {
        JobPostingDetailsResponseDto jobPosting = fetchJobPostingDetailsService.execute(jobPostingId);
        return ApiResponse.success(jobPosting);
    }
}
