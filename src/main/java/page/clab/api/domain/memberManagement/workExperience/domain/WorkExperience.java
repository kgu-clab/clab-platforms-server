package page.clab.api.domain.memberManagement.workExperience.domain;

import java.time.LocalDate;
import java.util.Optional;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import page.clab.api.domain.memberManagement.member.application.dto.shared.MemberDetailedInfoDto;
import page.clab.api.domain.memberManagement.workExperience.application.dto.request.WorkExperienceUpdateRequestDto;
import page.clab.api.global.exception.BaseException;
import page.clab.api.global.exception.ErrorCode;

@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class WorkExperience {

    private Long id;
    private String companyName;
    private String position;
    private LocalDate startDate;
    private LocalDate endDate;
    private String memberId;
    private Boolean isDeleted;

    public void update(WorkExperienceUpdateRequestDto workExperienceUpdateRequestDto) {
        Optional.ofNullable(workExperienceUpdateRequestDto.getCompanyName()).ifPresent(this::setCompanyName);
        Optional.ofNullable(workExperienceUpdateRequestDto.getPosition()).ifPresent(this::setPosition);
        Optional.ofNullable(workExperienceUpdateRequestDto.getStartDate()).ifPresent(this::setStartDate);
        Optional.ofNullable(workExperienceUpdateRequestDto.getEndDate()).ifPresent(this::setEndDate);
    }

    public void delete() {
        this.isDeleted = true;
    }

    public boolean isOwner(String memberId) {
        return this.memberId.equals(memberId);
    }

    public void validateBusinessRules() {
        if (startDate.isAfter(endDate)) {
            throw new BaseException(ErrorCode.INVALID_DATE_RANGE);
        }
    }

    public void validateAccessPermission(MemberDetailedInfoDto memberInfo) {
        if (!isOwner(memberInfo.getMemberId()) && !memberInfo.isSuperAdminRole()) {
            throw new BaseException(ErrorCode.PERMISSION_DENIED, "해당 경력사항을 수정/삭제할 권한이 없습니다.");
        }
    }
}
