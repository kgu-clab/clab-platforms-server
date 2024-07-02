package page.clab.api.domain.jobPosting.application;

import page.clab.api.domain.jobPosting.dto.request.JobPostingUpdateRequestDto;

public interface JobPostingUpdateUseCase {
    Long update(Long jobPostingId, JobPostingUpdateRequestDto requestDto);
}