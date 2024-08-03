package page.clab.api.domain.members.membershipFee.adapter.out.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface MembershipFeeRepository extends JpaRepository<MembershipFeeJpaEntity, Long>,
        MembershipFeeRepositoryCustom, QuerydslPredicateExecutor<MembershipFeeJpaEntity> {

    @Query(value = "SELECT m.* FROM membership_fee m WHERE m.is_deleted = true", nativeQuery = true)
    Page<MembershipFeeJpaEntity> findAllByIsDeletedTrue(Pageable pageable);
}
