package page.clab.api.domain.jobPosting.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.jobPosting.application.port.in.RetrieveDeletedJobPostingsUseCase;
import page.clab.api.domain.jobPosting.application.port.out.RetrieveJobPostingPort;
import page.clab.api.domain.jobPosting.domain.JobPosting;
import page.clab.api.domain.jobPosting.application.dto.response.JobPostingDetailsResponseDto;
import page.clab.api.global.common.dto.PagedResponseDto;

@Service
@RequiredArgsConstructor
public class DeletedJobPostingsRetrievalService implements RetrieveDeletedJobPostingsUseCase {

    private final RetrieveJobPostingPort retrieveJobPostingPort;

    @Transactional(readOnly = true)
    @Override
    public PagedResponseDto<JobPostingDetailsResponseDto> retrieveDeletedJobPostings(Pageable pageable) {
        Page<JobPosting> jobPostings = retrieveJobPostingPort.findAllByIsDeletedTrue(pageable);
        return new PagedResponseDto<>(jobPostings.map(JobPostingDetailsResponseDto::toDto));
    }
}
