package page.clab.api.domain.donation.dao;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import page.clab.api.domain.donation.domain.Donation;
import page.clab.api.domain.member.domain.Member;

public interface DonationRepository extends JpaRepository<Donation, Long>, DonationRepositoryCustom, QuerydslPredicateExecutor<Donation> {

    Page<Donation> findAllByOrderByCreatedAtDesc(Pageable pageable);

    Page<Donation> findByDonorOrderByCreatedAtDesc(Member member, Pageable pageable);

}
