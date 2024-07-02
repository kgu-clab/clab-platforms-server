package page.clab.api.domain.jobPosting.application;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.jobPosting.application.port.in.DeletedJobPostingsRetrievalUseCase;
import page.clab.api.domain.jobPosting.application.port.out.RetrieveDeletedJobPostingsPort;
import page.clab.api.domain.jobPosting.domain.JobPosting;
import page.clab.api.domain.jobPosting.dto.response.JobPostingDetailsResponseDto;
import page.clab.api.global.common.dto.PagedResponseDto;

@Service
@RequiredArgsConstructor
public class DeletedJobPostingsRetrievalService implements DeletedJobPostingsRetrievalUseCase {

    private final RetrieveDeletedJobPostingsPort retrieveDeletedJobPostingsPort;

    @Transactional(readOnly = true)
    @Override
    public PagedResponseDto<JobPostingDetailsResponseDto> retrieve(Pageable pageable) {
        Page<JobPosting> jobPostings = retrieveDeletedJobPostingsPort.findAllByIsDeletedTrue(pageable);
        return new PagedResponseDto<>(jobPostings.map(JobPostingDetailsResponseDto::toDto));
    }
}
