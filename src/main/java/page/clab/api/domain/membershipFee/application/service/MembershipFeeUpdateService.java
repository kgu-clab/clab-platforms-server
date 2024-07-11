package page.clab.api.domain.membershipFee.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.memberManagement.member.application.dto.shared.MemberDetailedInfoDto;
import page.clab.api.domain.memberManagement.member.application.port.in.RetrieveMemberInfoUseCase;
import page.clab.api.domain.membershipFee.application.dto.request.MembershipFeeUpdateRequestDto;
import page.clab.api.domain.membershipFee.application.port.in.UpdateMembershipFeeUseCase;
import page.clab.api.domain.membershipFee.application.port.out.RetrieveMembershipFeePort;
import page.clab.api.domain.membershipFee.application.port.out.UpdateMembershipFeePort;
import page.clab.api.domain.membershipFee.domain.MembershipFee;
import page.clab.api.global.exception.PermissionDeniedException;

@Service
@RequiredArgsConstructor
public class MembershipFeeUpdateService implements UpdateMembershipFeeUseCase {

    private final RetrieveMemberInfoUseCase retrieveMemberInfoUseCase;
    private final RetrieveMembershipFeePort retrieveMembershipFeePort;
    private final UpdateMembershipFeePort updateMembershipFeePort;

    @Transactional
    @Override
    public Long updateMembershipFee(Long membershipFeeId, MembershipFeeUpdateRequestDto requestDto) throws PermissionDeniedException {
        MemberDetailedInfoDto currentMemberInfo = retrieveMemberInfoUseCase.getCurrentMemberDetailedInfo();
        MembershipFee membershipFee = retrieveMembershipFeePort.findByIdOrThrow(membershipFeeId);
        membershipFee.validateAccessPermission(currentMemberInfo);
        membershipFee.update(requestDto);
        return updateMembershipFeePort.update(membershipFee).getId();
    }
}