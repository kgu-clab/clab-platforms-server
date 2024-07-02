package page.clab.api.domain.jobPosting.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.jobPosting.application.port.in.JobPostingDetailsRetrievalUseCase;
import page.clab.api.domain.jobPosting.application.port.out.LoadJobPostingPort;
import page.clab.api.domain.jobPosting.domain.JobPosting;
import page.clab.api.domain.jobPosting.dto.response.JobPostingDetailsResponseDto;

@Service
@RequiredArgsConstructor
public class JobPostingDetailsRetrievalService implements JobPostingDetailsRetrievalUseCase {

    private final LoadJobPostingPort loadJobPostingPort;

    @Transactional(readOnly = true)
    @Override
    public JobPostingDetailsResponseDto retrieve(Long jobPostingId) {
        JobPosting jobPosting = loadJobPostingPort.findByIdOrThrow(jobPostingId);
        return JobPostingDetailsResponseDto.toDto(jobPosting);
    }
}
