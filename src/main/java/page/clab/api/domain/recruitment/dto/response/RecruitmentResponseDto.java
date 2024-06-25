package page.clab.api.domain.recruitment.dto.response;

import lombok.Builder;
import lombok.Getter;
import page.clab.api.domain.application.domain.ApplicationType;
import page.clab.api.domain.recruitment.domain.Recruitment;
import page.clab.api.domain.recruitment.domain.RecruitmentStatus;

import java.time.LocalDateTime;

@Getter
@Builder
public class RecruitmentResponseDto {

    private Long id;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private ApplicationType applicationType;

    private String target;

    private RecruitmentStatus status;

    private LocalDateTime updatedAt;

    public static RecruitmentResponseDto toDto(Recruitment recruitment) {
        return RecruitmentResponseDto.builder()
                .id(recruitment.getId())
                .startDate(recruitment.getStartDate())
                .endDate(recruitment.getEndDate())
                .applicationType(recruitment.getApplicationType())
                .target(recruitment.getTarget())
                .status(recruitment.getStatus())
                .updatedAt(recruitment.getUpdatedAt())
                .build();
    }

}
