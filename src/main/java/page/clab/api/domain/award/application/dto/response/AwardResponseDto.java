package page.clab.api.domain.award.application.dto.response;

import lombok.Builder;
import lombok.Getter;
import page.clab.api.domain.award.domain.Award;

import java.time.LocalDate;

@Getter
@Builder
public class AwardResponseDto {

    private Long id;
    private String competitionName;
    private String organizer;
    private String awardName;
    private LocalDate awardDate;

    public static AwardResponseDto toDto(Award award) {
        return AwardResponseDto.builder()
                .id(award.getId())
                .competitionName(award.getCompetitionName())
                .organizer(award.getOrganizer())
                .awardName(award.getAwardName())
                .awardDate(award.getAwardDate())
                .build();
    }
}
