package page.clab.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import page.clab.api.type.entity.Schedule;

public interface ScheduleRepository  extends JpaRepository<Schedule, Long> {
}
