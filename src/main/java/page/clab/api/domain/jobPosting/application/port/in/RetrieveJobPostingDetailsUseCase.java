package page.clab.api.domain.jobPosting.application.port.in;

import page.clab.api.domain.jobPosting.dto.response.JobPostingDetailsResponseDto;

public interface RetrieveJobPostingDetailsUseCase {
    JobPostingDetailsResponseDto retrieve(Long jobPostingId);
}
