package page.clab.api.domain.membershipFee.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.member.application.MemberLookupUseCase;
import page.clab.api.domain.member.dto.shared.MemberDetailedInfoDto;
import page.clab.api.domain.membershipFee.application.port.in.MembershipFeeRemoveUseCase;
import page.clab.api.domain.membershipFee.application.port.out.LoadMembershipFeePort;
import page.clab.api.domain.membershipFee.application.port.out.RegisterMembershipFeePort;
import page.clab.api.domain.membershipFee.domain.MembershipFee;
import page.clab.api.global.exception.PermissionDeniedException;

@Service
@RequiredArgsConstructor
public class MembershipFeeRemoveService implements MembershipFeeRemoveUseCase {

    private final MemberLookupUseCase memberLookupUseCase;
    private final LoadMembershipFeePort loadMembershipFeePort;
    private final RegisterMembershipFeePort registerMembershipFeePort;

    @Transactional
    @Override
    public Long remove(Long membershipFeeId) throws PermissionDeniedException {
        MemberDetailedInfoDto currentMemberInfo = memberLookupUseCase.getCurrentMemberDetailedInfo();
        MembershipFee membershipFee = loadMembershipFeePort.findByIdOrThrow(membershipFeeId);
        membershipFee.validateAccessPermission(currentMemberInfo);
        membershipFee.delete();
        registerMembershipFeePort.save(membershipFee);
        return membershipFee.getId();
    }
}
