package page.clab.api.domain.jobPosting.application.port.in;

import page.clab.api.domain.jobPosting.application.dto.request.JobPostingUpdateRequestDto;

public interface UpdateJobPostingUseCase {
    Long updateJobPosting(Long jobPostingId, JobPostingUpdateRequestDto requestDto);
}
