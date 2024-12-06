package page.clab.api.global.common.slack.domain;

import lombok.Builder;
import lombok.Getter;
import page.clab.api.domain.memberManagement.member.application.dto.shared.MemberBasicInfoDto;
import page.clab.api.domain.members.membershipFee.domain.MembershipFee;

@Getter
@Builder
public class SlackMembershipFeeInfo {

    private String memberId;
    private String memberName;
    private String category;
    private Long amount;
    private String content;

    public static SlackMembershipFeeInfo create(MembershipFee membershipFee, MemberBasicInfoDto memberInfo) {
        return SlackMembershipFeeInfo.builder()
                .memberId(memberInfo.getMemberId())
                .memberName(memberInfo.getMemberName())
                .category(membershipFee.getCategory())
                .content(membershipFee.getContent())
                .amount(membershipFee.getAmount())
                .build();
    }
}
