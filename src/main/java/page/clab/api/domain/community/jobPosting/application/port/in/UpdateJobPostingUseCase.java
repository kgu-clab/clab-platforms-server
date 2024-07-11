package page.clab.api.domain.community.jobPosting.application.port.in;

import page.clab.api.domain.community.jobPosting.application.dto.request.JobPostingUpdateRequestDto;

public interface UpdateJobPostingUseCase {
    Long updateJobPosting(Long jobPostingId, JobPostingUpdateRequestDto requestDto);
}
