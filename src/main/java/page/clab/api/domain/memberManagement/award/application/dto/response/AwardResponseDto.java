package page.clab.api.domain.memberManagement.award.application.dto.response;

import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AwardResponseDto {

    private Long id;
    private String competitionName;
    private String organizer;
    private String awardName;
    private LocalDate awardDate;
}
