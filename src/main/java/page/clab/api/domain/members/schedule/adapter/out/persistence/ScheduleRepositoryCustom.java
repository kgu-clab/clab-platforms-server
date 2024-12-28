package page.clab.api.domain.members.schedule.adapter.out.persistence;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import page.clab.api.domain.activity.activitygroup.domain.ActivityGroup;
import page.clab.api.domain.members.schedule.application.dto.response.ScheduleCollectResponseDto;
import page.clab.api.domain.members.schedule.domain.SchedulePriority;

public interface ScheduleRepositoryCustom {

    Page<ScheduleJpaEntity> findByDateRangeAndMember(LocalDate startDate, LocalDate endDate,
        List<ActivityGroup> myGroups, Pageable pageable);

    Page<ScheduleJpaEntity> findByConditions(Integer year, Integer month, SchedulePriority priority, Pageable pageable);

    Page<ScheduleJpaEntity> findActivitySchedulesByDateRangeAndMemberId(LocalDate startDate, LocalDate endDate,
        String memberId, Pageable pageable);

    ScheduleCollectResponseDto findCollectSchedules();
}
