package page.clab.api.domain.members.membershipFee.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.memberManagement.member.application.port.in.RetrieveMemberUseCase;
import page.clab.api.domain.memberManagement.notification.application.port.in.SendNotificationUseCase;
import page.clab.api.domain.members.membershipFee.application.dto.request.MembershipFeeRequestDto;
import page.clab.api.domain.members.membershipFee.application.port.in.RegisterMembershipFeeUseCase;
import page.clab.api.domain.members.membershipFee.application.port.out.RegisterMembershipFeePort;
import page.clab.api.domain.members.membershipFee.domain.MembershipFee;

@Service
@RequiredArgsConstructor
public class MembershipFeeRegisterService implements RegisterMembershipFeeUseCase {

    private final RetrieveMemberUseCase retrieveMemberUseCase;
    private final SendNotificationUseCase notificationService;
    private final RegisterMembershipFeePort registerMembershipFeePort;

    @Transactional
    @Override
    public Long registerMembershipFee(MembershipFeeRequestDto requestDto) {
        String currentMemberId = retrieveMemberUseCase.getCurrentMemberId();
        MembershipFee membershipFee = MembershipFeeRequestDto.toEntity(requestDto, currentMemberId);
        notificationService.sendNotificationToAdmins("새로운 회비 내역이 등록되었습니다.");
        return registerMembershipFeePort.save(membershipFee).getId();
    }
}
