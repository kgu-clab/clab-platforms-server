package page.clab.api.domain.jobPosting.application.port.in;

import org.springframework.data.domain.Pageable;
import page.clab.api.domain.jobPosting.dto.response.JobPostingDetailsResponseDto;
import page.clab.api.global.common.dto.PagedResponseDto;

public interface RetrieveDeletedJobPostingsUseCase {
    PagedResponseDto<JobPostingDetailsResponseDto> retrieve(Pageable pageable);
}
