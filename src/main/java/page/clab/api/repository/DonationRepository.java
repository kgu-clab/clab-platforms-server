package page.clab.api.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import page.clab.api.type.entity.Donation;
import page.clab.api.type.entity.Member;

public interface DonationRepository extends JpaRepository<Donation, Long> {

    Page<Donation> findByDonor_Id(String memberId, Pageable pageable);

    Page<Donation> findByDonor_Name(String name, Pageable pageable);

    Page<Donation> findByDonor(Member member, Pageable pageable);

}
