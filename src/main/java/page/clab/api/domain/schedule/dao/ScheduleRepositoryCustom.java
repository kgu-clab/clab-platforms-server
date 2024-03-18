package page.clab.api.domain.schedule.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import page.clab.api.domain.member.domain.Member;
import page.clab.api.domain.schedule.domain.Schedule;

import java.time.LocalDate;

public interface ScheduleRepositoryCustom {

    Page<Schedule> findByDateRangeAndMember(LocalDate startDate, LocalDate endDate, Member member, Pageable pageable);

    Page<Schedule> findActivitySchedulesByDateRangeAndMember(LocalDate startDate, LocalDate endDate, Member member, Pageable pageable);

}
