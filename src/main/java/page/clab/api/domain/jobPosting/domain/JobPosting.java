package page.clab.api.domain.jobPosting.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.URL;
import page.clab.api.domain.jobPosting.dto.request.JobPostingRequestDto;
import page.clab.api.domain.jobPosting.dto.request.JobPostingUpdateRequestDto;
import page.clab.api.global.common.domain.BaseEntity;

import java.util.Optional;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class JobPosting extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Size(min = 1, max = 100, message = "{size.jobPosting.title}")
    private String title;

    @Enumerated(EnumType.STRING)
    private CareerLevel careerLevel;

    @Enumerated(EnumType.STRING)
    private EmploymentType employmentType;

    @Column(nullable = false)
    @Size(min = 1, message = "{size.jobPosting.companyName}")
    private String companyName;

    private String recruitmentPeriod;

    @Column(nullable = false)
    @Size(max = 1000, message = "{size.jobPosting.jobPostingUrl}")
    @URL(message = "{url.jobPosting.jobPostingUrl}")
    private String jobPostingUrl;

    public void update(JobPostingUpdateRequestDto jobPostingUpdateRequestDto) {
        Optional.ofNullable(jobPostingUpdateRequestDto.getTitle()).ifPresent(this::setTitle);
        Optional.ofNullable(jobPostingUpdateRequestDto.getCareerLevel()).ifPresent(this::setCareerLevel);
        Optional.ofNullable(jobPostingUpdateRequestDto.getEmploymentType()).ifPresent(this::setEmploymentType);
        Optional.ofNullable(jobPostingUpdateRequestDto.getCompanyName()).ifPresent(this::setCompanyName);
        Optional.ofNullable(jobPostingUpdateRequestDto.getRecruitmentPeriod()).ifPresent(this::setRecruitmentPeriod);
        Optional.ofNullable(jobPostingUpdateRequestDto.getJobPostingUrl()).ifPresent(this::setJobPostingUrl);
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
