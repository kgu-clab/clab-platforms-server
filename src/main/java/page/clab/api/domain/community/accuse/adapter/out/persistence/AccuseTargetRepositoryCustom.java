package page.clab.api.domain.community.accuse.adapter.out.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import page.clab.api.domain.community.accuse.domain.AccuseStatus;
import page.clab.api.domain.community.accuse.domain.TargetType;

public interface AccuseTargetRepositoryCustom {

    Page<AccuseTargetJpaEntity> findByConditions(TargetType type, AccuseStatus status, boolean countOrder,
        Pageable pageable);
}
