package page.clab.api.domain.membershipFee.application;

import page.clab.api.global.exception.PermissionDeniedException;

public interface MembershipFeeRemoveService {
    Long remove(Long membershipFeeId) throws PermissionDeniedException;
}
