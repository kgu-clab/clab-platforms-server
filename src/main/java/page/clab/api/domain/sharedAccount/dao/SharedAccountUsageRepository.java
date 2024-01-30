package page.clab.api.domain.sharedAccount.dao;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import page.clab.api.domain.sharedAccount.domain.SharedAccountUsage;
import page.clab.api.domain.sharedAccount.domain.SharedAccountUsageStatus;

public interface SharedAccountUsageRepository extends JpaRepository<SharedAccountUsage, Long> {

    Page<SharedAccountUsage> findAllByOrderByCreatedAtDesc(Pageable pageable);
    
    List<SharedAccountUsage> findByStatusAndEndTimeBefore(SharedAccountUsageStatus sharedAccountUsageStatus, LocalDateTime now);

}
