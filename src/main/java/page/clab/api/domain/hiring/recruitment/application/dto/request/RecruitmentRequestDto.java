package page.clab.api.domain.hiring.recruitment.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import page.clab.api.domain.hiring.application.domain.ApplicationType;

@Getter
@Setter
public class RecruitmentRequestDto {

    @NotNull(message = "{notNull.recruitment.title}")
    @Schema(description = "모집 공고 제목", example = "C-Lab Core Team 3기 모집", required = true)
    private String recruitmentTitle;

    @NotNull(message = "{notNull.recruitment.detail}")
    @Schema(description = "모집 공고 소개", example = "C-Lab Core Team 3기 모집", required = true)
    private String recruitmentDetail;

    @NotNull(message = "{notNull.recruitment.startDate}")
    @Schema(description = "모집 시작일", example = "2023-11-06T00:00:00", required = true)
    private LocalDateTime startDate;

    @NotNull(message = "{notNull.recruitment.endDate}")
    @Schema(description = "모집 종료일", example = "2023-11-08T00:00:00", required = true)
    private LocalDateTime endDate;

    @NotNull(message = "{notNull.recruitment.schedule}")
    @Schema(description = "모집 일정", example = "대면 면접 | 09월 10일(화)", required = true)
    private String recruitmentSchedule;

    @NotNull(message = "{notNull.recruitment.applicationType}")
    @Schema(description = "구분", example = "CORE_TEAM", required = true)
    private ApplicationType applicationType;

    @NotNull(message = "{notNull.recruitment.description}")
    @Schema(description = "설명", example = "실무에 가까운 경험을 쌓을 수 있어요.", required = true)
    private String description;

    @NotNull(message = "{notNull.recruitment.target}")
    @Schema(description = "대상", example = "2~3학년", required = true)
    private String target;
}
