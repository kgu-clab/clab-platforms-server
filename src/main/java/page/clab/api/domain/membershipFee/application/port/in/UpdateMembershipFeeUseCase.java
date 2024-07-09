package page.clab.api.domain.membershipFee.application.port.in;

import page.clab.api.domain.membershipFee.application.dto.request.MembershipFeeUpdateRequestDto;
import page.clab.api.global.exception.PermissionDeniedException;

public interface UpdateMembershipFeeUseCase {
    Long updateMembershipFee(Long membershipFeeId, MembershipFeeUpdateRequestDto requestDto) throws PermissionDeniedException;
}
