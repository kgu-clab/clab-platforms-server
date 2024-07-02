package page.clab.api.domain.donation.application.port.out;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import page.clab.api.domain.donation.domain.Donation;

import java.time.LocalDate;

public interface RetrieveDonationsByConditionsPort {
    Page<Donation> findByConditions(String memberId, String name, LocalDate startDate, LocalDate endDate, Pageable pageable);
}