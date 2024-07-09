package page.clab.api.domain.award.adapter.out.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import page.clab.api.domain.award.domain.Award;

public interface AwardRepositoryCustom {
    Page<Award> findByConditions(String memberId, Long year, Pageable pageable);
}
