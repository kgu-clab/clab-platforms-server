package page.clab.api.domain.jobPosting.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.validator.constraints.URL;
import page.clab.api.domain.jobPosting.dto.request.JobPostingRequestDto;
import page.clab.api.global.util.ModelMapperUtil;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(indexes = {@Index(name = "idx_job_posting_url", columnList = "jobPostingUrl")})
public class JobPosting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Size(min = 1, max = 100, message = "{size.jobPosting.title}")
    private String title;

    private CareerLevel careerLevel;

    private EmploymentType employmentType;

    @Column(nullable = false)
    @Size(min = 1, message = "{size.jobPosting.companyName}")
    private String companyName;

    private String recruitmentPeriod;

    @Column(nullable = false, length = 1000)
    @URL(message = "{url.jobPosting.jobPostingUrl}")
    private String jobPostingUrl;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    public static JobPosting of(JobPostingRequestDto jobPostingRequestDto) {
        return ModelMapperUtil.getModelMapper().map(jobPostingRequestDto, JobPosting.class);
    }

}
