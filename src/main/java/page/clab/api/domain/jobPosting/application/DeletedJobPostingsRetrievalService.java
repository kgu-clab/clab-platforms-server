package page.clab.api.domain.jobPosting.application;

import org.springframework.data.domain.Pageable;
import page.clab.api.domain.jobPosting.dto.response.JobPostingDetailsResponseDto;
import page.clab.api.global.common.dto.PagedResponseDto;

public interface DeletedJobPostingsRetrievalService {
    PagedResponseDto<JobPostingDetailsResponseDto> retrieveDeleted(Pageable pageable);
}