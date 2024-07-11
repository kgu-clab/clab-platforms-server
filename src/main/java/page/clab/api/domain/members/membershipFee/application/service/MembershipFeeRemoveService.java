package page.clab.api.domain.members.membershipFee.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.memberManagement.member.application.dto.shared.MemberDetailedInfoDto;
import page.clab.api.domain.memberManagement.member.application.port.in.RetrieveMemberInfoUseCase;
import page.clab.api.domain.members.membershipFee.application.port.in.RemoveMembershipFeeUseCase;
import page.clab.api.domain.members.membershipFee.application.port.out.RegisterMembershipFeePort;
import page.clab.api.domain.members.membershipFee.application.port.out.RetrieveMembershipFeePort;
import page.clab.api.domain.members.membershipFee.domain.MembershipFee;
import page.clab.api.global.exception.PermissionDeniedException;

@Service
@RequiredArgsConstructor
public class MembershipFeeRemoveService implements RemoveMembershipFeeUseCase {

    private final RetrieveMemberInfoUseCase retrieveMemberInfoUseCase;
    private final RetrieveMembershipFeePort retrieveMembershipFeePort;
    private final RegisterMembershipFeePort registerMembershipFeePort;

    @Transactional
    @Override
    public Long removeMembershipFee(Long membershipFeeId) throws PermissionDeniedException {
        MemberDetailedInfoDto currentMemberInfo = retrieveMemberInfoUseCase.getCurrentMemberDetailedInfo();
        MembershipFee membershipFee = retrieveMembershipFeePort.findByIdOrThrow(membershipFeeId);
        membershipFee.validateAccessPermission(currentMemberInfo);
        membershipFee.delete();
        registerMembershipFeePort.save(membershipFee);
        return membershipFee.getId();
    }
}
