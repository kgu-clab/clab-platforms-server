package page.clab.api.domain.activity.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import page.clab.api.domain.activity.domain.ActivityGroup;
import page.clab.api.domain.activity.domain.Attendance;

import java.time.LocalDate;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    Page<Attendance> findAllByMemberIdAndActivityGroup(String memberId, ActivityGroup activityGroup, Pageable pageable);

    Page<Attendance> findAllByActivityGroup(ActivityGroup activityGroup, Pageable pageable);

    Attendance findByActivityGroupAndMemberIdAndActivityDate(ActivityGroup activityGroup, String memberId, LocalDate activityDate);

    boolean existsByActivityGroupAndActivityDate(ActivityGroup activityGroup, LocalDate activityDate);
}
