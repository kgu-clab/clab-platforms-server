package page.clab.api.domain.membershipFee.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
import page.clab.api.domain.member.application.dto.shared.MemberDetailedInfoDto;
import page.clab.api.domain.membershipFee.application.dto.request.MembershipFeeUpdateRequestDto;
import page.clab.api.global.common.domain.BaseEntity;
import page.clab.api.global.exception.PermissionDeniedException;

import java.util.Optional;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@SQLDelete(sql = "UPDATE membership_fee SET is_deleted = true WHERE id = ?")
@SQLRestriction("is_deleted = false")
public class MembershipFee extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "member_id", nullable = false)
    private String memberId;

    @Column(nullable = false)
    @Size(min = 1, message = "{size.membershipFee.category}")
    private String category;

    private String account;

    @Column(nullable = false)
    private Long amount;

    @Column(nullable = false)
    @Size(min = 1, max = 1000, message = "{size.membershipFee.content}")
    private String content;

    private String imageUrl;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private MembershipFeeStatus status;

    public void update(MembershipFeeUpdateRequestDto membershipFeeUpdateRequestDto) {
        Optional.ofNullable(membershipFeeUpdateRequestDto.getCategory()).ifPresent(this::setCategory);
        Optional.ofNullable(membershipFeeUpdateRequestDto.getAccount()).ifPresent(this::setAccount);
        Optional.ofNullable(membershipFeeUpdateRequestDto.getAmount()).ifPresent(this::setAmount);
        Optional.ofNullable(membershipFeeUpdateRequestDto.getContent()).ifPresent(this::setContent);
        Optional.ofNullable(membershipFeeUpdateRequestDto.getImageUrl()).ifPresent(this::setImageUrl);
        Optional.ofNullable(membershipFeeUpdateRequestDto.getStatus()).ifPresent(this::setStatus);
    }

    public void delete() {
        this.isDeleted = true;
    }

    public boolean isOwner(String memberId) {
        return this.memberId.equals(memberId);
    }

    public void validateAccessPermission(MemberDetailedInfoDto memberInfo) throws PermissionDeniedException {
        if (!isOwner(memberInfo.getMemberId()) && !memberInfo.isAdminRole()) {
            throw new PermissionDeniedException("해당 회비를 수정/삭제할 권한이 없습니다.");
        }
    }
}
