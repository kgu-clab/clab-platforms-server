package page.clab.api.domain.members.schedule.adapter.out.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface ScheduleRepository extends JpaRepository<ScheduleJpaEntity, Long>, ScheduleRepositoryCustom, QuerydslPredicateExecutor<ScheduleJpaEntity> {

    @Query(value = "SELECT s.* FROM schedule s WHERE s.is_deleted = true", nativeQuery = true)
    Page<ScheduleJpaEntity> findAllByIsDeletedTrue(Pageable pageable);
}
