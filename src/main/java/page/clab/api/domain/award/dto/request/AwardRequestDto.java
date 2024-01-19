package page.clab.api.domain.award.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AwardRequestDto {

    @NotNull(message = "{notNull.award.competitionName}")
    @Size(min = 1, max = 255, message = "{size.award.competitionName}")
    @Schema(description = "대회명", example = "제10회 소프트웨어 개발보안 시큐어코딩 해커톤", required = true)
    private String competitionName;

    @NotNull(message = "{notNull.award.organizer}")
    @Size(min = 1, max = 255, message = "{size.award.organizer}")
    @Schema(description = "주최기관", example = "한국정보보호학회", required = true)
    private String organizer;

    @NotNull(message = "{notNull.award.awardName}")
    @Size(min = 1, max = 255, message = "{size.award.awardName}")
    @Schema(description = "수상명", example = "우수상", required = true)
    private String awardName;

    @NotNull(message = "{notNull.award.awardDate}")
    @Schema(description = "수상일", example = "2023-08-18", required = true)
    private LocalDate awardDate;

}
