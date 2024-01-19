package page.clab.api.domain.donation.dao;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import page.clab.api.domain.donation.domain.Donation;
import page.clab.api.domain.member.domain.Member;

public interface DonationRepository extends JpaRepository<Donation, Long> {

    Page<Donation> findAllByOrderByCreatedAtDesc(Pageable pageable);

    Page<Donation> findByDonor_IdOrderByCreatedAtDesc(String memberId, Pageable pageable);

    Page<Donation> findByDonor_NameOrderByCreatedAtDesc(String name, Pageable pageable);

    Page<Donation> findByDonorOrderByCreatedAtDesc(Member member, Pageable pageable);

}
