package page.clab.api.domain.workExperience.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import page.clab.api.domain.member.dto.shared.MemberDetailedInfoDto;
import page.clab.api.domain.workExperience.dto.request.WorkExperienceUpdateRequestDto;
import page.clab.api.global.common.domain.BaseEntity;
import page.clab.api.global.exception.PermissionDeniedException;

import java.time.LocalDate;
import java.util.Optional;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@SQLDelete(sql = "UPDATE work_experience SET is_deleted = true WHERE id = ?")
@SQLRestriction("is_deleted = false")
public class WorkExperience extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Size(min = 1, message = "{size.workExperience.companyName}")
    private String companyName;

    @Column(nullable = false)
    @Size(min = 1, message = "{size.workExperience.position}")
    private String position;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    @Column(name = "member_id", nullable = false)
    private String memberId;

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

    public void validateAccessPermission(MemberDetailedInfoDto memberInfo) throws PermissionDeniedException {
        if (!isOwner(memberInfo.getMemberId()) && !memberInfo.isSuperAdminRole()) {
            throw new PermissionDeniedException("해당 경력사항을 수정/삭제할 권한이 없습니다.");
        }
    }
}
