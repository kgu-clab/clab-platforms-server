package page.clab.api.domain.hiring.recruitment.application.dto.response;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import page.clab.api.domain.hiring.application.domain.ApplicationType;

@Getter
@Builder
public class RecruitmentDetailsResponseDto {

    private Long id;
    private String title;
    private String teamIntroduction;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String processTimeline;
    private ApplicationType applicationType;
    private String jobDescription;
    private String target;
    private String status;
    private LocalDateTime updatedAt;
}
