package page.clab.api.domain.donation.dao;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import page.clab.api.domain.donation.domain.Donation;

public interface DonationRepository extends JpaRepository<Donation, Long>, DonationRepositoryCustom, QuerydslPredicateExecutor<Donation> {

    Page<Donation> findAllByOrderByCreatedAtDesc(Pageable pageable);

    Page<Donation> findByMemberId(String memberId, Pageable pageable);

    @Query(value = "SELECT d.* FROM donation d WHERE d.is_deleted = true", nativeQuery = true)
    Page<Donation> findAllByIsDeletedTrue(Pageable pageable);
}
