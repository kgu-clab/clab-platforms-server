package page.clab.api.domain.donation.adapter.out.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public interface DonationRepositoryCustom {
    Page<DonationJpaEntity> findByConditions(String memberId, String name, LocalDate startDate, LocalDate endDate, Pageable pageable);
}
