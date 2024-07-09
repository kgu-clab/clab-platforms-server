package page.clab.api.domain.donation.adapter.out.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import page.clab.api.domain.donation.domain.Donation;

import java.time.LocalDate;

public interface DonationRepositoryCustom {
    Page<Donation> findByConditions(String memberId, String name, LocalDate startDate, LocalDate endDate, Pageable pageable);
}
