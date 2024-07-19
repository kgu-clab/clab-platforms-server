package page.clab.api.domain.community.jobPosting.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import page.clab.api.domain.community.jobPosting.application.dto.request.JobPostingRequestDto;
import page.clab.api.domain.community.jobPosting.application.dto.request.JobPostingUpdateRequestDto;

import java.time.LocalDateTime;
import java.util.Optional;

@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class JobPosting {

    private Long id;
    private String title;
    private CareerLevel careerLevel;
    private EmploymentType employmentType;
    private String companyName;
    private String recruitmentPeriod;
    private String jobPostingUrl;
    private Boolean isDeleted;
    private LocalDateTime createdAt;

    public void update(JobPostingUpdateRequestDto jobPostingUpdateRequestDto) {
        Optional.ofNullable(jobPostingUpdateRequestDto.getTitle()).ifPresent(this::setTitle);
        Optional.ofNullable(jobPostingUpdateRequestDto.getCareerLevel()).ifPresent(this::setCareerLevel);
        Optional.ofNullable(jobPostingUpdateRequestDto.getEmploymentType()).ifPresent(this::setEmploymentType);
        Optional.ofNullable(jobPostingUpdateRequestDto.getCompanyName()).ifPresent(this::setCompanyName);
        Optional.ofNullable(jobPostingUpdateRequestDto.getRecruitmentPeriod()).ifPresent(this::setRecruitmentPeriod);
        Optional.ofNullable(jobPostingUpdateRequestDto.getJobPostingUrl()).ifPresent(this::setJobPostingUrl);
    }

    public void delete() {
        this.isDeleted = true;
    }

    public JobPosting updateFromRequestDto(JobPostingRequestDto jobPostingRequestDto) {
        this.title = jobPostingRequestDto.getTitle();
        this.careerLevel = jobPostingRequestDto.getCareerLevel();
        this.employmentType = jobPostingRequestDto.getEmploymentType();
        this.companyName = jobPostingRequestDto.getCompanyName();
        this.recruitmentPeriod = jobPostingRequestDto.getRecruitmentPeriod();
        this.jobPostingUrl = jobPostingRequestDto.getJobPostingUrl();
        return this;
    }
}
