package page.clab.api.domain.schedule.adapter.out.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import page.clab.api.domain.schedule.domain.Schedule;

public interface ScheduleRepository extends JpaRepository<Schedule, Long>, ScheduleRepositoryCustom, QuerydslPredicateExecutor<Schedule> {

    @Query(value = "SELECT s.* FROM schedule s WHERE s.is_deleted = true", nativeQuery = true)
    Page<Schedule> findAllByIsDeletedTrue(Pageable pageable);
}
