package page.clab.api.domain.jobPosting.application.port.in;

import org.springframework.data.domain.Pageable;
import page.clab.api.domain.jobPosting.application.dto.response.JobPostingResponseDto;
import page.clab.api.domain.jobPosting.domain.CareerLevel;
import page.clab.api.domain.jobPosting.domain.EmploymentType;
import page.clab.api.global.common.dto.PagedResponseDto;

public interface RetrieveJobPostingsByConditionsUseCase {
    PagedResponseDto<JobPostingResponseDto> retrieveJobPostings(String title, String companyName, CareerLevel careerLevel, EmploymentType employmentType, Pageable pageable);
}
