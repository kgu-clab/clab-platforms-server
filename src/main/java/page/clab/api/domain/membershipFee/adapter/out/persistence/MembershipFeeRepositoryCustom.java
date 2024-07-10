package page.clab.api.domain.membershipFee.adapter.out.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import page.clab.api.domain.membershipFee.domain.MembershipFeeStatus;

public interface MembershipFeeRepositoryCustom {
    Page<MembershipFeeJpaEntity> findByConditions(String memberId, String memberName, String category, MembershipFeeStatus status, Pageable pageable);
}
