package page.clab.api.domain.schedule.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import page.clab.api.domain.member.domain.Member;
import page.clab.api.domain.schedule.domain.Schedule;

import java.time.LocalDateTime;

public interface ScheduleRepositoryCustom {

    Page<Schedule> findByDateRangeAndMember(LocalDateTime startDateTime, LocalDateTime endDateTime, Member member, Pageable pageable);

    Page<Schedule> findActivitySchedulesByDateRangeAndMember(LocalDateTime startDateTime, LocalDateTime endDateTime, Member member, Pageable pageable);

}
