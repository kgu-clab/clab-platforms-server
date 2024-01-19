package page.clab.api.domain.recruitment.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import page.clab.api.domain.application.domain.ApplicationType;
import page.clab.api.domain.recruitment.domain.Recruitment;
import page.clab.api.global.util.ModelMapperUtil;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RecruitmentRequestDto {

    @NotNull(message = "{notNull.recruitment.startDate}")
    @Schema(description = "모집 시작일", example = "2023-11-06T00:00:00", required = true)
    private LocalDateTime startDate;

    @NotNull(message = "{notNull.recruitment.endDate}")
    @Schema(description = "모집 종료일", example = "2023-11-08T00:00:00", required = true)
    private LocalDateTime endDate;

    @NotNull(message = "{notNull.recruitment.applicationType}")
    @Schema(description = "구분", example = "CORE_TEAM", required = true)
    private ApplicationType applicationType;

    @NotNull(message = "{notNull.recruitment.target}")
    @Size(min = 1, message = "{size.recruitment.target}")
    @Schema(description = "대상", example = "2~3학년", required = true)
    private String target;

    @NotNull(message = "{notNull.recruitment.status}")
    @Size(min = 1, message = "{size.recruitment.status}")
    @Schema(description = "상태", example = "종료", required = true)
    private String status;

    public static RecruitmentRequestDto of(Recruitment recruitment) {
        return ModelMapperUtil.getModelMapper().map(recruitment, RecruitmentRequestDto.class);
    }

}
