package page.clab.api.domain.schedule.adapter.out.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import page.clab.api.domain.activityGroup.domain.ActivityGroup;
import page.clab.api.domain.member.domain.Member;
import page.clab.api.domain.schedule.application.dto.response.ScheduleCollectResponseDto;
import page.clab.api.domain.schedule.domain.SchedulePriority;

import java.time.LocalDate;
import java.util.List;

public interface ScheduleRepositoryCustom {

    Page<ScheduleJpaEntity> findByDateRangeAndMember(LocalDate startDate, LocalDate endDate, List<ActivityGroup> myGroups, Pageable pageable);

    Page<ScheduleJpaEntity> findByConditions(Integer year, Integer month, SchedulePriority priority, Pageable pageable);

    Page<ScheduleJpaEntity> findActivitySchedulesByDateRangeAndMember(LocalDate startDate, LocalDate endDate, Member member, Pageable pageable);

    ScheduleCollectResponseDto findCollectSchedules();
}
