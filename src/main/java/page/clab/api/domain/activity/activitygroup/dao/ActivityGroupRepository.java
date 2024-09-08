package page.clab.api.domain.activity.activitygroup.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;
import page.clab.api.domain.activity.activitygroup.domain.ActivityGroup;
import page.clab.api.domain.activity.activitygroup.domain.ActivityGroupCategory;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ActivityGroupRepository extends JpaRepository<ActivityGroup, Long>, ActivityGroupRepositoryCustom, QuerydslPredicateExecutor<ActivityGroup> {

    Page<ActivityGroup> findAllByCategory(ActivityGroupCategory category, Pageable pageable);

    @Query(value = "SELECT a.* FROM activity_group a WHERE a.is_deleted = true", nativeQuery = true)
    Page<ActivityGroup> findAllByIsDeletedTrue(Pageable pageable);

    List<ActivityGroup> findByEndDateBefore(LocalDate now);
}
