package page.clab.api.domain.jobPosting.dto.response;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import page.clab.api.domain.jobPosting.domain.CareerLevel;
import page.clab.api.domain.jobPosting.domain.EmploymentType;
import page.clab.api.domain.jobPosting.domain.JobPosting;
import page.clab.api.global.util.ModelMapperUtil;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
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

    public static JobPostingDetailsResponseDto of(JobPosting jobPosting) {
        return ModelMapperUtil.getModelMapper().map(jobPosting, JobPostingDetailsResponseDto.class);
    }

}
