package page.clab.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import page.clab.api.type.entity.Attendance;
import page.clab.api.type.entity.AttendanceId;

public interface AttendanceRepository extends JpaRepository<Attendance, AttendanceId> {

}
