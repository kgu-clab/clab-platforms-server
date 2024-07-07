package page.clab.api.domain.recruitment.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import page.clab.api.domain.application.domain.ApplicationType;

import java.time.LocalDateTime;

@Getter
@Setter
public class RecruitmentUpdateRequestDto {

    @Schema(description = "모집 시작일", example = "2023-11-06T00:00:00")
    private LocalDateTime startDate;

    @Schema(description = "모집 종료일", example = "2023-11-08T00:00:00")
    private LocalDateTime endDate;

    @Schema(description = "구분", example = "CORE_TEAM")
    private ApplicationType applicationType;

    @Schema(description = "대상", example = "2~3학년")
    private String target;
}
