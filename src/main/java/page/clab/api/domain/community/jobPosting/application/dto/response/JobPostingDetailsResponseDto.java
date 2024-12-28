package page.clab.api.domain.community.jobPosting.application.dto.response;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import page.clab.api.domain.community.jobPosting.domain.CareerLevel;
import page.clab.api.domain.community.jobPosting.domain.EmploymentType;

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
}
