package page.clab.api.domain.accuse.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import page.clab.api.domain.accuse.domain.AccuseStatus;
import page.clab.api.domain.accuse.domain.AccuseTarget;
import page.clab.api.domain.accuse.domain.TargetType;

public interface AccuseTargetRepositoryCustom {
    Page<AccuseTarget> findByConditions(TargetType type, AccuseStatus status, boolean countOrder, Pageable pageable);
}
