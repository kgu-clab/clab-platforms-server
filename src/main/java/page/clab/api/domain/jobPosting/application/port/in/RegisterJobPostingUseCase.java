package page.clab.api.domain.jobPosting.application.port.in;

import page.clab.api.domain.jobPosting.application.dto.request.JobPostingRequestDto;

public interface RegisterJobPostingUseCase {
    Long registerJobPosting(JobPostingRequestDto requestDto);
}
