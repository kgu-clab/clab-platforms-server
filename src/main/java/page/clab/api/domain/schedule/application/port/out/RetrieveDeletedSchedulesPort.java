package page.clab.api.domain.schedule.application.port.out;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import page.clab.api.domain.schedule.domain.Schedule;

public interface RetrieveDeletedSchedulesPort {
    Page<Schedule> findAllByIsDeletedTrue(Pageable pageable);
}
