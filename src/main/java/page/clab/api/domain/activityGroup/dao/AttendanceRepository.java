package page.clab.api.domain.activityGroup.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import page.clab.api.domain.activityGroup.domain.ActivityGroup;
import page.clab.api.domain.activityGroup.domain.Attendance;
import page.clab.api.domain.member.domain.Member;

import java.time.LocalDate;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    Page<Attendance> findAllByMemberAndActivityGroup(Member member, ActivityGroup activityGroup, Pageable pageable);
    Page<Attendance> findAllByActivityGroup(ActivityGroup activityGroup, Pageable pageable);
    Attendance findByActivityGroupAndMemberAndActivityDate(ActivityGroup activityGroup, Member member, LocalDate activityDate);
    boolean existsByActivityGroupAndActivityDate(ActivityGroup activityGroup, LocalDate activityDate);
}
