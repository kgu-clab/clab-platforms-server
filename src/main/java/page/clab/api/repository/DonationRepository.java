package page.clab.api.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import page.clab.api.type.entity.Donation;
import page.clab.api.type.entity.Member;

public interface DonationRepository extends JpaRepository<Donation, Long> {

    List<Donation> findByDonor_Id(String memberId);

    List<Donation> findByDonor_Name(String name);

    List<Donation> findByDonor(Member member);

}
