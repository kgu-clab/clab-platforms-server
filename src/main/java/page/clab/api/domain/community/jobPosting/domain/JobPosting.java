package page.clab.api.domain.community.jobPosting.domain;

import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
}
