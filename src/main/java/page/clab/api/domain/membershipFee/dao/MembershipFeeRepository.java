package page.clab.api.domain.membershipFee.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import page.clab.api.domain.membershipFee.domain.MembershipFee;

public interface MembershipFeeRepository extends JpaRepository<MembershipFee, Long>, MembershipFeeRepositoryCustom, QuerydslPredicateExecutor<MembershipFee> {
    @Query(value = "SELECT m.* FROM membership_fee m WHERE m.is_deleted = true", nativeQuery = true)
    Page<MembershipFee> findAllByIsDeletedTrue(Pageable pageable);
}
