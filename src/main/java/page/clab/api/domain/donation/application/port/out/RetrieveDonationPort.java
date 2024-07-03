package page.clab.api.domain.donation.application.port.out;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import page.clab.api.domain.donation.domain.Donation;

import java.time.LocalDate;
import java.util.Optional;

public interface RetrieveDonationPort {
    Optional<Donation> findById(Long donationId);
    Donation findByIdOrThrow(Long donationId);
    Page<Donation> findAllByIsDeletedTrue(Pageable pageable);
    Page<Donation> findByConditions(String memberId, String name, LocalDate startDate, LocalDate endDate, Pageable pageable);
    Page<Donation> findByMemberId(String memberId, Pageable pageable);
}
