package page.clab.api.domain.membershipFee.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import page.clab.api.domain.membershipFee.domain.MembershipFee;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MembershipFeeResponseDto {

    private Long id;

    private String memberId;

    private String memberName;

    private String category;

    private Long amount;

    private String content;

    private String imageUrl;

    private LocalDateTime createdAt;

    public static MembershipFeeResponseDto toDto(MembershipFee membershipFee) {
        return MembershipFeeResponseDto.builder()
                .id(membershipFee.getId())
                .memberId(membershipFee.getApplicant().getId())
                .memberName(membershipFee.getApplicant().getName())
                .category(membershipFee.getCategory())
                .amount(membershipFee.getAmount())
                .content(membershipFee.getContent())
                .imageUrl(membershipFee.getImageUrl())
                .createdAt(membershipFee.getCreatedAt())
                .build();
    }

}
