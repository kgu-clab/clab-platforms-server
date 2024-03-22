package page.clab.api.domain.award.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import page.clab.api.domain.award.domain.Award;
import page.clab.api.domain.member.domain.Member;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AwardRequestDto {

    @NotNull(message = "{notNull.award.competitionName}")
    @Schema(description = "대회명", example = "제10회 소프트웨어 개발보안 시큐어코딩 해커톤", required = true)
    private String competitionName;

    @NotNull(message = "{notNull.award.organizer}")
    @Schema(description = "주최기관", example = "한국정보보호학회", required = true)
    private String organizer;

    @NotNull(message = "{notNull.award.awardName}")
    @Schema(description = "수상명", example = "우수상", required = true)
    private String awardName;

    @NotNull(message = "{notNull.award.awardDate}")
    @Schema(description = "수상일", example = "2023-08-18", required = true)
    private LocalDate awardDate;

    public static Award toEntity(AwardRequestDto requestDto, Member member) {
        return Award.builder()
                .competitionName(requestDto.getCompetitionName())
                .organizer(requestDto.getOrganizer())
                .awardName(requestDto.getAwardName())
                .awardDate(requestDto.getAwardDate())
                .member(member)
                .build();
    }

}
