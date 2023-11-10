package page.clab.api.type.dto;

import java.time.LocalDateTime;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import page.clab.api.type.entity.Recruitment;
import page.clab.api.type.etc.ApplicationType;
import page.clab.api.util.ModelMapperUtil;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RecruitmentRequestDto {

    @NotNull(message = "{notNull.recruitment.startDate}")
    private LocalDateTime startDate;

    @NotNull(message = "{notNull.recruitment.endDate}")
    private LocalDateTime endDate;

    @NotNull(message = "{notNull.recruitment.applicationType}")
    private ApplicationType applicationType;

    @NotNull(message = "{notNull.recruitment.target}")
    @Size(min = 1, message = "{size.recruitment.target}")
    private String target;

    @NotNull(message = "{notNull.recruitment.status}")
    @Size(min = 1, message = "{size.recruitment.status}")
    private String status;

    public static RecruitmentRequestDto of(Recruitment recruitment) {
        return ModelMapperUtil.getModelMapper().map(recruitment, RecruitmentRequestDto.class);
    }

}
