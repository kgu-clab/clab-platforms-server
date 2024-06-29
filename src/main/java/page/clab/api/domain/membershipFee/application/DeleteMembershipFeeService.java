package page.clab.api.domain.membershipFee.application;

import page.clab.api.global.exception.PermissionDeniedException;

public interface DeleteMembershipFeeService {
    Long execute(Long membershipFeeId) throws PermissionDeniedException;
}
