package page.clab.api.domain.membershipFee.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.member.application.port.in.RetrieveMemberInfoUseCase;
import page.clab.api.domain.member.dto.shared.MemberDetailedInfoDto;
import page.clab.api.domain.membershipFee.application.port.in.UpdateMembershipFeeUseCase;
import page.clab.api.domain.membershipFee.application.port.out.RetrieveMembershipFeePort;
import page.clab.api.domain.membershipFee.application.port.out.UpdateMembershipFeePort;
import page.clab.api.domain.membershipFee.domain.MembershipFee;
import page.clab.api.domain.membershipFee.dto.request.MembershipFeeUpdateRequestDto;
import page.clab.api.global.exception.PermissionDeniedException;
import page.clab.api.global.validation.ValidationService;

@Service
@RequiredArgsConstructor
public class MembershipFeeUpdateService implements UpdateMembershipFeeUseCase {

    private final RetrieveMemberInfoUseCase retrieveMemberInfoUseCase;
    private final ValidationService validationService;
    private final RetrieveMembershipFeePort retrieveMembershipFeePort;
    private final UpdateMembershipFeePort updateMembershipFeePort;

    @Transactional
    @Override
    public Long update(Long membershipFeeId, MembershipFeeUpdateRequestDto requestDto) throws PermissionDeniedException {
        MemberDetailedInfoDto currentMemberInfo = retrieveMemberInfoUseCase.getCurrentMemberDetailedInfo();
        MembershipFee membershipFee = retrieveMembershipFeePort.findByIdOrThrow(membershipFeeId);
        membershipFee.validateAccessPermission(currentMemberInfo);
        membershipFee.update(requestDto);
        validationService.checkValid(membershipFee);
        return updateMembershipFeePort.update(membershipFee).getId();
    }
}
