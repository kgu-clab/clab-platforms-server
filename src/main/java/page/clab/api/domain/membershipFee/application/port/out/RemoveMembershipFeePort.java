package page.clab.api.domain.membershipFee.application.port.out;

import page.clab.api.domain.membershipFee.domain.MembershipFee;

public interface RemoveMembershipFeePort {
    void delete(MembershipFee membershipFee);
}