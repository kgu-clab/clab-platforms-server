package page.clab.api.domain.jobPosting.application;

import page.clab.api.domain.jobPosting.dto.request.JobPostingRequestDto;

public interface CreateJobPostingService {
    Long execute(JobPostingRequestDto requestDto);
}
