package page.clab.api.domain.accuse.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import page.clab.api.domain.accuse.domain.AccuseTarget;
import page.clab.api.domain.accuse.domain.AccuseTargetId;

public interface AccuseTargetRepository extends JpaRepository<AccuseTarget, AccuseTargetId> {

}
