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
import page.clab.api.domain.hiring.application.domain.ApplicationType;
import page.clab.api.domain.hiring.recruitment.application.dto.request.RecruitmentUpdateRequestDto;
import page.clab.api.global.exception.BaseException;
import page.clab.api.global.exception.ErrorCode;

@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Recruitment {

    private Long id;
    private String title;
    private String teamIntroduction;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String processTimeline;
    private ApplicationType applicationType;
    private String jobDescription;
    private String target;
    private RecruitmentStatus status;
    private Boolean isDeleted;
    private LocalDateTime updatedAt;

    public void update(RecruitmentUpdateRequestDto recruitmentUpdateRequestDto) {
        Optional.ofNullable(recruitmentUpdateRequestDto.getTitle()).ifPresent(this::setTitle);
        Optional.ofNullable(recruitmentUpdateRequestDto.getTeamIntroduction()).ifPresent(this::setTeamIntroduction);
        Optional.ofNullable(recruitmentUpdateRequestDto.getStartDate()).ifPresent(this::setStartDate);
        Optional.ofNullable(recruitmentUpdateRequestDto.getEndDate()).ifPresent(this::setEndDate);
        Optional.ofNullable(recruitmentUpdateRequestDto.getProcessTimeline()).ifPresent(this::setProcessTimeline);
        Optional.ofNullable(recruitmentUpdateRequestDto.getApplicationType()).ifPresent(this::setApplicationType);
        Optional.ofNullable(recruitmentUpdateRequestDto.getJobDescription()).ifPresent(this::setJobDescription);
        Optional.ofNullable(recruitmentUpdateRequestDto.getTarget()).ifPresent(this::setTarget);
    }

    public void delete() {
        this.isDeleted = true;
    }

    public void updateStatus(RecruitmentStatus status) {
        this.status = status;
    }

    public void validateRecruiting() {
        LocalDateTime now = LocalDateTime.now();
        if (!status.isRecruiting()) {
            throw new BaseException(ErrorCode.RECRUITMENT_NOT_ACTIVE, "진행 중인 모집 공고가 아닙니다.");
        }
        if (now.isBefore(startDate) || now.isAfter(endDate)) {
            throw new BaseException(ErrorCode.RECRUITMENT_NOT_ACTIVE, "모집 기간이 아닙니다.");
        }
    }

    public void validateDateRange() {
        if (startDate.isAfter(endDate)) {
            throw new BaseException(ErrorCode.INVALID_DATE_RANGE);
        }
    }

    /**
     * 모집 종료일이 현재 날짜 기준 7일 이내인지 확인하고, 그렇지 않은 경우 예외를 발생시킵니다.
     *
     * <p>예외 발생 상황:</p>
     * <ul>
     *   <li>{@code BaseException} with {@code ErrorCode.RECRUITMENT_END_DATE_EXCEEDED} - 모집 종료일이 현재 날짜 기준 7일을 초과했거나, 아직 모집이 종료되지 않은 경우</li>
     * </ul>
     *
     * @throws BaseException 위의 상황에 따라 {@code ErrorCode.RECRUITMENT_END_DATE_EXCEEDED}가 발생합니다.
     */
    public void validateEndDateWithin7Days() {
        LocalDate today = LocalDate.now();
        LocalDate endDateLocal = this.endDate.toLocalDate();

        if (endDateLocal.isBefore(today.minusDays(7)) || !this.isRecruitmentEnd()) {
            throw new BaseException(ErrorCode.INVALID_RECRUITMENT_CLOSURE_WINDOW);
        }
    }

    public boolean isRecruitmentEnd() {
        return LocalDateTime.now().isAfter(endDate) || status.isClosed();
    }
}
