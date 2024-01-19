package page.clab.api.domain.award.dto.response;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import page.clab.api.domain.award.domain.Award;
import page.clab.api.global.util.ModelMapperUtil;

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

    public static AwardResponseDto of(Award award) {
        return ModelMapperUtil.getModelMapper().map(award, AwardResponseDto.class);
    }

}
