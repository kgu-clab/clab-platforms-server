package page.clab.api.domain.accuse.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import page.clab.api.domain.accuse.domain.Accuse;
import page.clab.api.domain.accuse.domain.AccuseStatus;
import page.clab.api.domain.accuse.domain.TargetType;

public interface AccuseRepositoryCustom {

    Page<Accuse> findByConditions(TargetType targetType, AccuseStatus accuseStatus, Pageable pageable);

}
