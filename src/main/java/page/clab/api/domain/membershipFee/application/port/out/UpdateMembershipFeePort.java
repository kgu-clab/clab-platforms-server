package page.clab.api.domain.membershipFee.application.port.out;

import page.clab.api.domain.membershipFee.domain.MembershipFee;

public interface UpdateMembershipFeePort {
    MembershipFee update(MembershipFee membershipFee);
}
