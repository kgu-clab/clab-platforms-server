package page.clab.api.domain.members.schedule.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface ScheduleRepository extends JpaRepository<ScheduleJpaEntity, Long>, ScheduleRepositoryCustom,
    QuerydslPredicateExecutor<ScheduleJpaEntity> {

}
