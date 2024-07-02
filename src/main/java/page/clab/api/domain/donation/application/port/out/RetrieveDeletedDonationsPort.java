package page.clab.api.domain.donation.application.port.out;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import page.clab.api.domain.donation.domain.Donation;

public interface RetrieveDeletedDonationsPort {
    Page<Donation> findAllByIsDeletedTrue(Pageable pageable);
}