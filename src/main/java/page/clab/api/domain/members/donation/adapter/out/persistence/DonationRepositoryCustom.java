package page.clab.api.domain.members.donation.adapter.out.persistence;

import java.time.LocalDate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DonationRepositoryCustom {

    Page<DonationJpaEntity> findByConditions(String memberId, String name, LocalDate startDate, LocalDate endDate,
        Pageable pageable);
}
