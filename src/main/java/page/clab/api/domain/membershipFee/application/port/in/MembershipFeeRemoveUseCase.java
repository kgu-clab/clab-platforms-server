package page.clab.api.domain.membershipFee.application.port.in;

import page.clab.api.global.exception.PermissionDeniedException;

public interface MembershipFeeRemoveUseCase {
    Long remove(Long membershipFeeId) throws PermissionDeniedException;
}
