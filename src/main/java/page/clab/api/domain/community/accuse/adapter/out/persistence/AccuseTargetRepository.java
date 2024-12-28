package page.clab.api.domain.community.accuse.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface AccuseTargetRepository extends JpaRepository<AccuseTargetJpaEntity, AccuseTargetId>,
    AccuseTargetRepositoryCustom, QuerydslPredicateExecutor<AccuseTargetJpaEntity> {

}
