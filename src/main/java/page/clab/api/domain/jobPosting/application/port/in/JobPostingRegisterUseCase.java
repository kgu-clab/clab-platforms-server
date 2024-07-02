package page.clab.api.domain.jobPosting.application.port.in;

import page.clab.api.domain.jobPosting.dto.request.JobPostingRequestDto;

public interface JobPostingRegisterUseCase {
    Long register(JobPostingRequestDto requestDto);
}
