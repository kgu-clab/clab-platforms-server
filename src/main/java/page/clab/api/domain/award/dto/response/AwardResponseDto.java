package page.clab.api.domain.award.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import page.clab.api.domain.award.domain.Award;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
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
