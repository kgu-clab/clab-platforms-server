package page.clab.api.type.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import page.clab.api.type.entity.Award;
import page.clab.api.util.ModelMapperUtil;

import java.time.LocalDateTime;

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

    private String participants;

    private LocalDateTime awardDate;

    public static AwardResponseDto of(Award award) {
        return ModelMapperUtil.getModelMapper().map(award, AwardResponseDto.class);
    }

}
