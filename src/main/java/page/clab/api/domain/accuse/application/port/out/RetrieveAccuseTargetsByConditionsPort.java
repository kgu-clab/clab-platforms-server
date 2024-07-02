package page.clab.api.domain.accuse.application.port.out;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import page.clab.api.domain.accuse.domain.AccuseStatus;
import page.clab.api.domain.accuse.domain.AccuseTarget;
import page.clab.api.domain.accuse.domain.TargetType;

public interface RetrieveAccuseTargetsByConditionsPort {
    Page<AccuseTarget> findByConditions(TargetType type, AccuseStatus status, boolean countOrder, Pageable pageable);
}
