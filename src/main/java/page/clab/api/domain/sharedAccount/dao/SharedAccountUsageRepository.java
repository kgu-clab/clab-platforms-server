package page.clab.api.domain.sharedAccount.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import page.clab.api.domain.sharedAccount.domain.SharedAccountUsage;
import page.clab.api.domain.sharedAccount.domain.SharedAccountUsageStatus;

import java.time.LocalDateTime;
import java.util.List;

public interface SharedAccountUsageRepository extends JpaRepository<SharedAccountUsage, Long> {

    Page<SharedAccountUsage> findAllByOrderByCreatedAtDesc(Pageable pageable);

    List<SharedAccountUsage> findByStatusAndStartTimeBefore(SharedAccountUsageStatus sharedAccountUsageStatus, LocalDateTime currentDateTime);

    List<SharedAccountUsage> findByStatusAndEndTimeBefore(SharedAccountUsageStatus sharedAccountUsageStatus, LocalDateTime now);

    List<SharedAccountUsage> findBySharedAccountIdAndStatusAndStartTimeBeforeAndEndTimeAfter(Long sharedAccountId, SharedAccountUsageStatus sharedAccountUsageStatus, LocalDateTime endTime, LocalDateTime startTime);

}
