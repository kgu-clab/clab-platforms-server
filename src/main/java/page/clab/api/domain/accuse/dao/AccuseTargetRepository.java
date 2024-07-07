package page.clab.api.domain.accuse.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import page.clab.api.domain.accuse.domain.AccuseTarget;
import page.clab.api.domain.accuse.domain.AccuseTargetId;

public interface AccuseTargetRepository extends JpaRepository<AccuseTarget, AccuseTargetId>, AccuseTargetRepositoryCustom, QuerydslPredicateExecutor<AccuseTarget> {
}
