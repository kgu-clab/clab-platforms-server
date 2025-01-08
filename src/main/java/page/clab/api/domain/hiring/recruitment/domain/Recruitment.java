package page.clab.api.domain.hiring.recruitment.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import page.clab.api.domain.hiring.application.application.exception.RecruitmentEndDateExceededException;
import page.clab.api.domain.hiring.application.application.exception.RecruitmentNotActiveException;
import page.clab.api.domain.hiring.application.domain.ApplicationType;
import page.clab.api.domain.hiring.recruitment.application.dto.request.RecruitmentUpdateRequestDto;
import page.clab.api.global.exception.InvalidDateRangeException;

@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Recruitment {

    private Long id;
    private String recruitmentTitle;
    private String recruitmentDetail;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String recruitmentSchedule;
    private ApplicationType applicationType;
    private String description;
    private String target;
    private RecruitmentStatus status;
    private Boolean isDeleted;
    private LocalDateTime updatedAt;

    public void update(RecruitmentUpdateRequestDto recruitmentUpdateRequestDto) {
        Optional.ofNullable(recruitmentUpdateRequestDto.getRecruitmentTitle()).ifPresent(this::setRecruitmentTitle);
        Optional.ofNullable(recruitmentUpdateRequestDto.getRecruitmentDetail()).ifPresent(this::setRecruitmentDetail);
        Optional.ofNullable(recruitmentUpdateRequestDto.getStartDate()).ifPresent(this::setStartDate);
        Optional.ofNullable(recruitmentUpdateRequestDto.getEndDate()).ifPresent(this::setEndDate);
        Optional.ofNullable(recruitmentUpdateRequestDto.getRecruitmentSchedule()).ifPresent(this::setRecruitmentSchedule);
        Optional.ofNullable(recruitmentUpdateRequestDto.getApplicationType()).ifPresent(this::setApplicationType);
        Optional.ofNullable(recruitmentUpdateRequestDto.getDescription()).ifPresent(this::setDescription);
        Optional.ofNullable(recruitmentUpdateRequestDto.getTarget()).ifPresent(this::setTarget);
    }

    public void updateStatus(RecruitmentStatus status) {
        this.status = status;
    }

    public void validateRecruiting() {
        LocalDateTime now = LocalDateTime.now();
        if (!status.isRecruiting()) {
            throw new RecruitmentNotActiveException("진행 중인 모집 공고가 아닙니다.");
        }
        if (now.isBefore(startDate) || now.isAfter(endDate)) {
            throw new RecruitmentNotActiveException("모집 기간이 아닙니다.");
        }
    }

    public void validateDateRange() {
        if (startDate.isAfter(endDate)) {
            throw new InvalidDateRangeException("시작일은 종료일보다 늦을 수 없습니다.");
        }
    }

    /**
     * 모집 종료일이 현재 날짜 기준 7일 이내인지 확인하고, 그렇지 않은 경우 예외를 발생시킵니다.
     *
     * @throws RecruitmentEndDateExceededException 모집 종료일이 현재 날짜 기준 7일을 초과했거나, 아직 모집이 종료되지 않은 경우 발생
     */
    public void validateEndDateWithin7Days() {
        LocalDate today = LocalDate.now();
        LocalDate endDate = this.endDate.toLocalDate();

        if (endDate.isBefore(today.minusDays(7)) || !this.isRecruitmentEnd()) {
            throw new RecruitmentEndDateExceededException("지원 기간이 종료된 지 7일이 넘었거나, 아직 종료되지 않은 모집입니다.");
        }
    }

    public boolean isRecruitmentEnd() {
        return LocalDateTime.now().isAfter(endDate) || status.isClosed();
    }
}
