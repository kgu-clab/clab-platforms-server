package page.clab.api.domain.sharedAccount.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import page.clab.api.domain.sharedAccount.domain.SharedAccount;
import page.clab.api.domain.sharedAccount.domain.SharedAccountUsage;
import page.clab.api.domain.sharedAccount.domain.SharedAccountUsageStatus;

import java.time.LocalDateTime;
import java.util.List;

public interface SharedAccountUsageRepository extends JpaRepository<SharedAccountUsage, Long> {

    Page<SharedAccountUsage> findAll(Pageable pageable);

    List<SharedAccountUsage> findByStatusAndEndTimeBefore(SharedAccountUsageStatus sharedAccountUsageStatus, LocalDateTime now);

    List<SharedAccountUsage> findByStatusAndStartTimeBeforeAndEndTimeAfter(SharedAccountUsageStatus sharedAccountUsageStatus, LocalDateTime currentDateTime, LocalDateTime currentDateTime1);

    List<SharedAccountUsage> findBySharedAccountIdAndStatusAndStartTimeBeforeAndEndTimeAfter(Long sharedAccountId, SharedAccountUsageStatus sharedAccountUsageStatus, LocalDateTime endTime, LocalDateTime startTime);

    boolean existsBySharedAccountAndStatusAndEndTimeAfter(SharedAccount sharedAccount, SharedAccountUsageStatus sharedAccountUsageStatus, LocalDateTime now);

}
