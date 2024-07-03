package page.clab.api.domain.jobPosting.application;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.jobPosting.application.port.in.RetrieveJobPostingsByConditionsUseCase;
import page.clab.api.domain.jobPosting.application.port.out.RetrieveJobPostingsByConditionsPort;
import page.clab.api.domain.jobPosting.domain.CareerLevel;
import page.clab.api.domain.jobPosting.domain.EmploymentType;
import page.clab.api.domain.jobPosting.domain.JobPosting;
import page.clab.api.domain.jobPosting.dto.response.JobPostingResponseDto;
import page.clab.api.global.common.dto.PagedResponseDto;

@Service
@RequiredArgsConstructor
public class JobPostingsByConditionsRetrievalService implements RetrieveJobPostingsByConditionsUseCase {

    private final RetrieveJobPostingsByConditionsPort retrieveJobPostingsByConditionsPort;

    @Transactional(readOnly = true)
    @Override
    public PagedResponseDto<JobPostingResponseDto> retrieve(String title, String companyName, CareerLevel careerLevel, EmploymentType employmentType, Pageable pageable) {
        Page<JobPosting> jobPostings = retrieveJobPostingsByConditionsPort.findByConditions(title, companyName, careerLevel, employmentType, pageable);
        return new PagedResponseDto<>(jobPostings.map(JobPostingResponseDto::toDto));
    }
}
