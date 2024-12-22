package page.clab.api.domain.community.accuse.application.port.out;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import page.clab.api.domain.community.accuse.adapter.out.persistence.AccuseTargetId;
import page.clab.api.domain.community.accuse.domain.AccuseStatus;
import page.clab.api.domain.community.accuse.domain.AccuseTarget;
import page.clab.api.domain.community.accuse.domain.TargetType;

public interface RetrieveAccuseTargetPort {

    Optional<AccuseTarget> findById(AccuseTargetId accuseTargetId);

    AccuseTarget getById(AccuseTargetId accuseTargetId);

    Page<AccuseTarget> findByConditions(TargetType type, AccuseStatus status, boolean countOrder, Pageable pageable);
}
