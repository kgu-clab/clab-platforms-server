package page.clab.api.domain.community.jobPosting.application.dto.mapper;

import org.springframework.stereotype.Component;
import page.clab.api.domain.community.jobPosting.application.dto.response.JobPostingDetailsResponseDto;
import page.clab.api.domain.community.jobPosting.application.dto.response.JobPostingResponseDto;
import page.clab.api.domain.community.jobPosting.domain.JobPosting;

@Component
public class JobPostingDtoMapper {

    public JobPostingResponseDto toDto(JobPosting jobPosting) {
        return JobPostingResponseDto.builder()
            .id(jobPosting.getId())
            .title(jobPosting.getTitle())
            .recruitmentPeriod(jobPosting.getRecruitmentPeriod())
            .jobPostingUrl(jobPosting.getJobPostingUrl())
            .createdAt(jobPosting.getCreatedAt())
            .build();
    }

    public JobPostingDetailsResponseDto toDetailsDto(JobPosting jobPosting) {
        return JobPostingDetailsResponseDto.builder()
            .id(jobPosting.getId())
            .title(jobPosting.getTitle())
            .careerLevel(jobPosting.getCareerLevel())
            .employmentType(jobPosting.getEmploymentType())
            .companyName(jobPosting.getCompanyName())
            .recruitmentPeriod(jobPosting.getRecruitmentPeriod())
            .jobPostingUrl(jobPosting.getJobPostingUrl())
            .createdAt(jobPosting.getCreatedAt())
            .build();
    }
}
