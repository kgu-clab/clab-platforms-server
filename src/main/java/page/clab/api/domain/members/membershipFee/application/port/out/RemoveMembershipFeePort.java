package page.clab.api.domain.members.membershipFee.application.port.out;

import page.clab.api.domain.members.membershipFee.domain.MembershipFee;

public interface RemoveMembershipFeePort {
    void delete(MembershipFee membershipFee);
}
