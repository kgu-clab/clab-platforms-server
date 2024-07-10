package page.clab.api.domain.position.adapter.out.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import page.clab.api.domain.position.domain.PositionType;

public interface PositionRepositoryCustom {
    Page<PositionJpaEntity> findByConditions(String year, PositionType positionType, Pageable pageable);
}
