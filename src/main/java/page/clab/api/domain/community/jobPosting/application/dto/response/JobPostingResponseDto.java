package page.clab.api.domain.community.jobPosting.application.dto.response;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class JobPostingResponseDto {

    private Long id;
    private String title;
    private String recruitmentPeriod;
    private String jobPostingUrl;
    private LocalDateTime createdAt;
}
