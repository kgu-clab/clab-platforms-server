package page.clab.api.domain.jobPosting.application;

import page.clab.api.domain.jobPosting.dto.response.JobPostingDetailsResponseDto;

public interface FetchJobPostingDetailsService {
    JobPostingDetailsResponseDto execute(Long jobPostingId);
}