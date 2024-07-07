package page.clab.api.domain.membershipFee.application.port.in;

import page.clab.api.global.exception.PermissionDeniedException;

public interface RemoveMembershipFeeUseCase {
    Long removeMembershipFee(Long membershipFeeId) throws PermissionDeniedException;
}
