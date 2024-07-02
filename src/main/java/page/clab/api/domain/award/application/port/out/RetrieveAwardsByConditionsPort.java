package page.clab.api.domain.award.application.port.out;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import page.clab.api.domain.award.domain.Award;

public interface RetrieveAwardsByConditionsPort {
    Page<Award> findByConditions(String memberId, Long year, Pageable pageable);
}
