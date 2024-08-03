package page.clab.api.domain.members.donation.adapter.out.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface DonationRepository extends JpaRepository<DonationJpaEntity, Long>, DonationRepositoryCustom, QuerydslPredicateExecutor<DonationJpaEntity> {

    Page<DonationJpaEntity> findByMemberId(String memberId, Pageable pageable);

    @Query(value = "SELECT d.* FROM donation d WHERE d.is_deleted = true", nativeQuery = true)
    Page<DonationJpaEntity> findAllByIsDeletedTrue(Pageable pageable);
}
