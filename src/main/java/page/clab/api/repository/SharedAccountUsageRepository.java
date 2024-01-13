package page.clab.api.repository;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import page.clab.api.type.entity.SharedAccountUsage;
import page.clab.api.type.etc.SharedAccountUsageStatus;

public interface SharedAccountUsageRepository extends JpaRepository<SharedAccountUsage, Long> {

    Page<SharedAccountUsage> findAllByOrderByCreatedAtDesc(Pageable pageable);
    
    List<SharedAccountUsage> findByStatusAndEndTimeBefore(SharedAccountUsageStatus sharedAccountUsageStatus, LocalDateTime now);

}
