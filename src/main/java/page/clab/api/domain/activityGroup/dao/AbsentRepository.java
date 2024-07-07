package page.clab.api.domain.activityGroup.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import page.clab.api.domain.activityGroup.domain.Absent;
import page.clab.api.domain.activityGroup.domain.ActivityGroup;
import page.clab.api.domain.member.domain.Member;

import java.time.LocalDate;

@Repository
public interface AbsentRepository extends JpaRepository<Absent, Long> {
    Page<Absent> findAllByActivityGroup(ActivityGroup activityGroup, Pageable pageable);

    Absent findByActivityGroupAndAbsenteeAndAbsentDate(ActivityGroup activityGroup, Member absentee, LocalDate absentDate);
}
