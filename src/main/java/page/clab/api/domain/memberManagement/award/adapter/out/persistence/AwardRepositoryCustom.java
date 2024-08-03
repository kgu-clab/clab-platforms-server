package page.clab.api.domain.memberManagement.award.adapter.out.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AwardRepositoryCustom {
    Page<AwardJpaEntity> findByConditions(String memberId, Long year, Pageable pageable);
}
