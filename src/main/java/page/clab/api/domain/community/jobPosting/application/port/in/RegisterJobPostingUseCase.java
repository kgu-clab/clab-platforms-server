package page.clab.api.domain.community.jobPosting.application.port.in;

import page.clab.api.domain.community.jobPosting.application.dto.request.JobPostingRequestDto;

public interface RegisterJobPostingUseCase {
    Long registerJobPosting(JobPostingRequestDto requestDto);
}
