package page.clab.api.domain.hiring.recruitment.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import page.clab.api.domain.hiring.application.domain.ApplicationType;

@Getter
@Setter
public class RecruitmentUpdateRequestDto {

    @Schema(description = "모집 공고 제목", example = "C-Lab Core Team 3기 모집")
    private String recruitmentTitle;

    @Schema(description = "모집 공고 소개", example = "C-Lab Core Team 3기 모집")
    private String recruitmentDetail;

    @Schema(description = "모집 시작일", example = "2023-11-06T00:00:00")
    private LocalDateTime startDate;

    @Schema(description = "모집 종료일", example = "2023-11-08T00:00:00")
    private LocalDateTime endDate;

    @Schema(description = "모집 일정", example = "대면 면접 | 09월 10일(화)")
    private String recruitmentSchedule;

    @Schema(description = "구분", example = "CORE_TEAM")
    private ApplicationType applicationType;

    @Schema(description = "설명", example = "실무에 가까운 경험을 쌓을 수 있어요.")
    private String recruitmentDescription;

    @Schema(description = "대상", example = "2~3학년")
    private String target;
}
