package page.clab.api.domain.membershipFee.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import page.clab.api.domain.membershipFee.domain.MembershipFee;

public interface MembershipFeeRepositoryCustom {

    Page<MembershipFee> findByConditions(String memberId, String memberName, String category, Pageable pageable);

}
