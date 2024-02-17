package page.clab.api.domain.recruitment.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import page.clab.api.domain.application.domain.ApplicationType;
import page.clab.api.domain.recruitment.domain.Recruitment;
import page.clab.api.global.util.ModelMapperUtil;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RecruitmentUpdateRequestDto {

    @Schema(description = "모집 시작일", example = "2023-11-06T00:00:00")
    private LocalDateTime startDate;

    @Schema(description = "모집 종료일", example = "2023-11-08T00:00:00")
    private LocalDateTime endDate;

    @Schema(description = "구분", example = "CORE_TEAM")
    private ApplicationType applicationType;

    @Size(min = 1, message = "{size.recruitment.target}")
    @Schema(description = "대상", example = "2~3학년")
    private String target;

    @Size(min = 1, message = "{size.recruitment.status}")
    @Schema(description = "상태", example = "종료")
    private String status;

    public static RecruitmentUpdateRequestDto of(Recruitment recruitment) {
        return ModelMapperUtil.getModelMapper().map(recruitment, RecruitmentUpdateRequestDto.class);
    }

}
