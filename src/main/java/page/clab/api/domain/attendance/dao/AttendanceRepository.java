package page.clab.api.domain.attendance.dao;

import java.time.LocalDate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import page.clab.api.domain.activityGroup.domain.ActivityGroup;
import page.clab.api.domain.attendance.domain.Attendance;
import page.clab.api.domain.member.domain.Member;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

        Page<Attendance> findAllByMemberAndActivityGroupOrderByCreatedAt(Member member, ActivityGroup activityGroup, Pageable pageable);

        Page<Attendance> findAllByActivityGroupOrderByActivityDateAscMemberAsc(ActivityGroup activityGroup, Pageable pageable);

        Attendance findByActivityGroupAndMemberAndActivityDate(ActivityGroup activityGroup, Member member, LocalDate activityDate);

        boolean existsByActivityGroupAndActivityDate(ActivityGroup activityGroup, LocalDate activityDate);


}
