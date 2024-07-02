package page.clab.api.domain.membershipFee.application.port.out;

import page.clab.api.domain.membershipFee.domain.MembershipFee;

import java.util.Optional;

public interface LoadMembershipFeePort {
    Optional<MembershipFee> findById(Long id);
    MembershipFee findByIdOrThrow(Long id);
}
