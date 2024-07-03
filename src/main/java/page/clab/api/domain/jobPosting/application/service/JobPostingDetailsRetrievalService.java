package page.clab.api.domain.jobPosting.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.jobPosting.application.port.in.RetrieveJobPostingDetailsUseCase;
import page.clab.api.domain.jobPosting.application.port.out.RetrieveJobPostingPort;
import page.clab.api.domain.jobPosting.domain.JobPosting;
import page.clab.api.domain.jobPosting.dto.response.JobPostingDetailsResponseDto;

@Service
@RequiredArgsConstructor
public class JobPostingDetailsRetrievalService implements RetrieveJobPostingDetailsUseCase {

    private final RetrieveJobPostingPort retrieveJobPostingPort;

    @Transactional(readOnly = true)
    @Override
    public JobPostingDetailsResponseDto retrieve(Long jobPostingId) {
        JobPosting jobPosting = retrieveJobPostingPort.findByIdOrThrow(jobPostingId);
        return JobPostingDetailsResponseDto.toDto(jobPosting);
    }
}
