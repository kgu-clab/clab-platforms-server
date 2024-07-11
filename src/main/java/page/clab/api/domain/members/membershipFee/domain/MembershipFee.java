package page.clab.api.domain.members.membershipFee.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import page.clab.api.domain.memberManagement.member.application.dto.shared.MemberDetailedInfoDto;
import page.clab.api.domain.members.membershipFee.application.dto.request.MembershipFeeUpdateRequestDto;
import page.clab.api.global.exception.PermissionDeniedException;

import java.time.LocalDateTime;
import java.util.Optional;

@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MembershipFee {

    private Long id;
    private String memberId;
    private String category;
    private String account;
    private Long amount;
    private String content;
    private String imageUrl;
    private MembershipFeeStatus status;
    private boolean isDeleted = false;
    private LocalDateTime createdAt;

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
