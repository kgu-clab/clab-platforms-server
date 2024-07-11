package page.clab.api.domain.activity.activitygroup.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import page.clab.api.domain.activity.activitygroup.domain.Absent;
import page.clab.api.domain.activity.activitygroup.domain.ActivityGroup;

import java.time.LocalDate;

@Repository
public interface AbsentRepository extends JpaRepository<Absent, Long> {
    Page<Absent> findAllByActivityGroup(ActivityGroup activityGroup, Pageable pageable);

    Absent findByActivityGroupAndMemberIdAndAbsentDate(ActivityGroup activityGroup, String memberId, LocalDate absentDate);
}
