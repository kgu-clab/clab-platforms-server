package page.clab.api.domain.memberManagement.award.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AwardUpdateRequestDto {

    @Schema(description = "대회명", example = "제10회 소프트웨어 개발보안 시큐어코딩 해커톤")
    private String competitionName;

    @Schema(description = "주최기관", example = "한국정보보호학회")
    private String organizer;

    @Schema(description = "수상명", example = "우수상")
    private String awardName;

    @Schema(description = "수상일", example = "2023-08-18")
    private LocalDate awardDate;
}
