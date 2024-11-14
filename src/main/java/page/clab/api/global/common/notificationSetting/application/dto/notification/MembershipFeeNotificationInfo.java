package page.clab.api.global.common.notificationSetting.application.dto.notification;

import lombok.Builder;
import lombok.Getter;
import page.clab.api.domain.memberManagement.member.application.dto.shared.MemberBasicInfoDto;
import page.clab.api.domain.members.membershipFee.domain.MembershipFee;

@Getter
@Builder
public class MembershipFeeNotificationInfo {

    private String memberId;
    private String memberName;
    private String category;
    private Long amount;
    private String content;

    public static MembershipFeeNotificationInfo create(MembershipFee membershipFee, MemberBasicInfoDto memberInfo) {
        return MembershipFeeNotificationInfo.builder()
                .memberId(memberInfo.getMemberId())
                .memberName(memberInfo.getMemberName())
                .category(membershipFee.getCategory())
                .content(membershipFee.getContent())
                .amount(membershipFee.getAmount())
                .build();
    }
}
