package page.clab.api.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import page.clab.api.type.entity.Schedule;

import java.time.LocalDateTime;

public interface ScheduleRepository  extends JpaRepository<Schedule, Long> {
        Page<Schedule> findAllByStartDateAfterAndEndDateBefore(LocalDateTime startDateTime, LocalDateTime endDateTime, Pageable pageable);
}
