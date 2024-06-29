package page.clab.api.domain.jobPosting.application;

import page.clab.api.domain.jobPosting.dto.response.JobPostingDetailsResponseDto;

public interface JobPostingDetailsRetrievalService {
    JobPostingDetailsResponseDto retrieve(Long jobPostingId);
}