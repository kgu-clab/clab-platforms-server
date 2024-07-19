package page.clab.api.domain.community.jobPosting.adapter.in.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.domain.community.jobPosting.application.dto.request.JobPostingRequestDto;
import page.clab.api.domain.community.jobPosting.application.port.in.RegisterJobPostingUseCase;
import page.clab.api.global.common.dto.ApiResponse;

@RestController
@RequestMapping("/api/v1/job-postings")
@RequiredArgsConstructor
@Tag(name = "Community - Job Posting", description = "커뮤니티 채용 공고")
public class JobPostingRegisterController {

    private final RegisterJobPostingUseCase registerJobPostingUseCase;

    @Operation(summary = "[A] 채용 공고 등록", description = "ROLE_ADMIN 이상의 권한이 필요함")
    @Secured({ "ROLE_ADMIN", "ROLE_SUPER" })
    @PostMapping("")
    public ApiResponse<Long> registerJobPosting(
            @Valid @RequestBody JobPostingRequestDto requestDto
    ) {
        Long id = registerJobPostingUseCase.registerJobPosting(requestDto);
        return ApiResponse.success(id);
    }
}
