package page.clab.api.domain.jobPosting.application;

import page.clab.api.domain.jobPosting.dto.request.JobPostingUpdateRequestDto;

public interface JobPostingUpdateService {
    Long update(Long jobPostingId, JobPostingUpdateRequestDto requestDto);
}