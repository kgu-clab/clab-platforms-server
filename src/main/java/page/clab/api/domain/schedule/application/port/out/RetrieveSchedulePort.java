package page.clab.api.domain.schedule.application.port.out;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import page.clab.api.domain.activity.activitygroup.domain.ActivityGroup;
import page.clab.api.domain.schedule.application.dto.response.ScheduleCollectResponseDto;
import page.clab.api.domain.schedule.domain.Schedule;
import page.clab.api.domain.schedule.domain.SchedulePriority;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface RetrieveSchedulePort {
    Optional<Schedule> findById(Long id);

    Schedule findByIdOrThrow(Long id);

    Page<Schedule> findAllByIsDeletedTrue(Pageable pageable);

    Page<Schedule> findByConditions(Integer year, Integer month, SchedulePriority priority, Pageable pageable);

    Page<Schedule> findActivitySchedulesByDateRangeAndMemberId(LocalDate startDate, LocalDate endDate, String memberId, Pageable pageable);

    ScheduleCollectResponseDto findCollectSchedules();

    Page<Schedule> findByDateRangeAndMember(LocalDate startDate, LocalDate endDate, List<ActivityGroup> myGroups, Pageable pageable);
}
