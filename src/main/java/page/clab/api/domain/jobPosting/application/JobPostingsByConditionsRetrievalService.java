package page.clab.api.domain.jobPosting.application;

import org.springframework.data.domain.Pageable;
import page.clab.api.domain.jobPosting.domain.CareerLevel;
import page.clab.api.domain.jobPosting.domain.EmploymentType;
import page.clab.api.domain.jobPosting.dto.response.JobPostingResponseDto;
import page.clab.api.global.common.dto.PagedResponseDto;

public interface JobPostingsByConditionsRetrievalService {
    PagedResponseDto<JobPostingResponseDto> retrieveByConditions(String title, String companyName, CareerLevel careerLevel, EmploymentType employmentType, Pageable pageable);
}