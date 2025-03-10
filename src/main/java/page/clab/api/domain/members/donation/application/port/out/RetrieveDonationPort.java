package page.clab.api.domain.members.donation.application.port.out;

import java.time.LocalDate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import page.clab.api.domain.members.donation.domain.Donation;

public interface RetrieveDonationPort {

    Donation getById(Long donationId);

    Page<Donation> findAllByIsDeletedTrue(Pageable pageable);

    Page<Donation> findByConditions(String memberId, String name, LocalDate startDate, LocalDate endDate,
        Pageable pageable);

    Page<Donation> findByMemberId(String memberId, Pageable pageable);
}
