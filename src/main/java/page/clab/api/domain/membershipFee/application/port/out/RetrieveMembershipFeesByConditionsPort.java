package page.clab.api.domain.membershipFee.application.port.out;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import page.clab.api.domain.membershipFee.domain.MembershipFee;
import page.clab.api.domain.membershipFee.domain.MembershipFeeStatus;

public interface RetrieveMembershipFeesByConditionsPort {
    Page<MembershipFee> findByConditions(String memberId, String memberName, String category, MembershipFeeStatus status, Pageable pageable);
}
