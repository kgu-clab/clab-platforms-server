package page.clab.api.domain.award.application.port.out;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import page.clab.api.domain.award.domain.Award;

public interface RetrieveDeletedAwardsPort {
    Page<Award> findAllByIsDeletedTrue(Pageable pageable);
}
