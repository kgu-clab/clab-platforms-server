package page.clab.api.domain.membershipFee.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import page.clab.api.domain.membershipFee.domain.MembershipFee;

public interface MembershipFeeRepository extends JpaRepository<MembershipFee, Long> {

    Page<MembershipFee> findAllByOrderByCreatedAtDesc(Pageable pageable);

    Page<MembershipFee> findByCategoryOrderByCreatedAtDesc(String category, Pageable pageable);

}
