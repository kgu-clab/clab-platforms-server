package page.clab.api.domain.members.membershipFee.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.members.membershipFee.application.dto.request.MembershipFeeRequestDto;
import page.clab.api.domain.members.membershipFee.application.port.in.RegisterMembershipFeeUseCase;
import page.clab.api.domain.members.membershipFee.application.port.out.RegisterMembershipFeePort;
import page.clab.api.domain.members.membershipFee.domain.MembershipFee;
import page.clab.api.external.memberManagement.member.application.port.ExternalRetrieveMemberUseCase;
import page.clab.api.external.memberManagement.notification.application.port.ExternalSendNotificationUseCase;

@Service
@RequiredArgsConstructor
public class MembershipFeeRegisterService implements RegisterMembershipFeeUseCase {

    private final RegisterMembershipFeePort registerMembershipFeePort;
    private final ExternalRetrieveMemberUseCase externalRetrieveMemberUseCase;
    private final ExternalSendNotificationUseCase externalSendNotificationUseCase;

    @Transactional
    @Override
    public Long registerMembershipFee(MembershipFeeRequestDto requestDto) {
        String currentMemberId = externalRetrieveMemberUseCase.getCurrentMemberId();
        MembershipFee membershipFee = MembershipFeeRequestDto.toEntity(requestDto, currentMemberId);
        externalSendNotificationUseCase.sendNotificationToAdmins("새로운 회비 내역이 등록되었습니다.");
        return registerMembershipFeePort.save(membershipFee).getId();
    }
}
