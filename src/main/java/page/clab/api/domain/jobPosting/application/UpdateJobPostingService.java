package page.clab.api.domain.jobPosting.application;

import page.clab.api.domain.jobPosting.dto.request.JobPostingUpdateRequestDto;

public interface UpdateJobPostingService {
    Long execute(Long jobPostingId, JobPostingUpdateRequestDto requestDto);
}