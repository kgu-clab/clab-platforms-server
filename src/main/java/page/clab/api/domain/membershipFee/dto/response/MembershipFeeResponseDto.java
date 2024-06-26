package page.clab.api.domain.membershipFee.dto.response;

import lombok.Builder;
import lombok.Getter;
import page.clab.api.domain.membershipFee.domain.MembershipFee;
import page.clab.api.domain.membershipFee.domain.MembershipFeeStatus;

import java.time.LocalDateTime;

@Getter
@Builder
public class MembershipFeeResponseDto {

    private Long id;

    private String memberId;

    private String memberName;

    private String category;

    private String account;

    private Long amount;

    private String content;

    private String imageUrl;

    private MembershipFeeStatus status;

    private LocalDateTime createdAt;

    public static MembershipFeeResponseDto toDto(MembershipFee membershipFee, String memberName, boolean isAdminRole) {
        return MembershipFeeResponseDto.builder()
                .id(membershipFee.getId())
                .memberId(membershipFee.getMemberId())
                .memberName(memberName)
                .category(membershipFee.getCategory())
                .account(isAdminRole ? membershipFee.getAccount() : null)
                .amount(membershipFee.getAmount())
                .content(membershipFee.getContent())
                .imageUrl(membershipFee.getImageUrl())
                .status(membershipFee.getStatus())
                .createdAt(membershipFee.getCreatedAt())
                .build();
    }

}
