package page.clab.api.type.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import page.clab.api.type.entity.MembershipFee;
import page.clab.api.util.ModelMapperUtil;

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

    private String content;

    private String imageUrl;

    private LocalDateTime createdAt;

    public static MembershipFeeResponseDto of(MembershipFee membershipFee) {
        MembershipFeeResponseDto membershipFeeResponseDto = ModelMapperUtil.getModelMapper()
                .map(membershipFee, MembershipFeeResponseDto.class);
        membershipFeeResponseDto.setMemberId(membershipFee.getApplicant().getId());
        membershipFeeResponseDto.setMemberName(membershipFee.getApplicant().getName());
        return membershipFeeResponseDto;
    }

}
