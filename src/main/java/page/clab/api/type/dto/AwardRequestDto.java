package page.clab.api.type.dto;

import java.time.LocalDate;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

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
    private String competitionName;

    @NotNull(message = "{notNull.award.organizer}")
    @Size(min = 1, max = 255, message = "{size.award.organizer}")
    private String organizer;

    @NotNull(message = "{notNull.award.awardName}")
    @Size(min = 1, max = 255, message = "{size.award.awardName}")
    private String awardName;

    @NotNull(message = "{notNull.award.awardDate}")
    private LocalDate awardDate;

}
