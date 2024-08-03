package page.clab.api.domain.hiring.recruitment.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import page.clab.api.domain.hiring.application.domain.ApplicationType;
import page.clab.api.domain.hiring.recruitment.application.dto.request.RecruitmentUpdateRequestDto;
import page.clab.api.global.exception.InvalidDateRangeException;

import java.time.LocalDateTime;
import java.util.Optional;

@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Recruitment {

    private Long id;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private ApplicationType applicationType;
    private String target;
    private RecruitmentStatus status;
    private Boolean isDeleted;
    private LocalDateTime updatedAt;

    public void update(RecruitmentUpdateRequestDto recruitmentUpdateRequestDto) {
        Optional.ofNullable(recruitmentUpdateRequestDto.getStartDate()).ifPresent(this::setStartDate);
        Optional.ofNullable(recruitmentUpdateRequestDto.getEndDate()).ifPresent(this::setEndDate);
        Optional.ofNullable(recruitmentUpdateRequestDto.getApplicationType()).ifPresent(this::setApplicationType);
        Optional.ofNullable(recruitmentUpdateRequestDto.getTarget()).ifPresent(this::setTarget);
    }

    public void updateStatus(RecruitmentStatus status) {
        this.status = status;
    }

    public void validateDateRange() {
        if (startDate.isAfter(endDate)) {
            throw new InvalidDateRangeException("시작일은 종료일보다 늦을 수 없습니다.");
        }
    }
}
