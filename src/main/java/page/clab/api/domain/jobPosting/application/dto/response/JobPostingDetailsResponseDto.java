package page.clab.api.domain.jobPosting.application.dto.response;

import lombok.Builder;
import lombok.Getter;
import page.clab.api.domain.jobPosting.domain.CareerLevel;
import page.clab.api.domain.jobPosting.domain.EmploymentType;
import page.clab.api.domain.jobPosting.domain.JobPosting;

import java.time.LocalDateTime;

@Getter
@Builder
public class JobPostingDetailsResponseDto {

    private Long id;

    private String title;

    private CareerLevel careerLevel;

    private EmploymentType employmentType;

    private String companyName;

    private String recruitmentPeriod;

    private String jobPostingUrl;

    private LocalDateTime createdAt;

    public static JobPostingDetailsResponseDto toDto(JobPosting jobPosting) {
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
