package page.clab.api.type.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
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

    @NotNull
    private String competitionName;

    @NotNull
    private String organizer;

    @NotNull
    private String awardName;

    @NotNull
    private LocalDateTime awardDate;

}
