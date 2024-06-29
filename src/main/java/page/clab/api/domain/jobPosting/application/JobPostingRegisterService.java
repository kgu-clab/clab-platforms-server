package page.clab.api.domain.jobPosting.application;

import page.clab.api.domain.jobPosting.dto.request.JobPostingRequestDto;

public interface JobPostingRegisterService {
    Long register(JobPostingRequestDto requestDto);
}
