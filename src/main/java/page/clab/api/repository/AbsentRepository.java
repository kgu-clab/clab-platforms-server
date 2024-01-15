package page.clab.api.repository;

import java.time.LocalDate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import page.clab.api.type.entity.Absent;
import page.clab.api.type.entity.ActivityGroup;
import page.clab.api.type.entity.Member;

@Repository
public interface AbsentRepository extends JpaRepository<Absent, Long> {

    Page<Absent> findAllByActivityGroup(ActivityGroup activityGroup, Pageable pageable);

    Absent findByActivityGroupAndAbsenteeAndAbsentDate(ActivityGroup activityGroup, Member absentee, LocalDate absentDate);

}
