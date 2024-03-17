package page.clab.api.domain.schedule.dao;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import page.clab.api.domain.member.domain.Member;
import page.clab.api.domain.schedule.domain.QSchedule;
import page.clab.api.domain.schedule.domain.Schedule;
import page.clab.api.domain.schedule.domain.ScheduleType;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ScheduleRepositoryImpl implements ScheduleRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Schedule> findByDateRangeAndMember(LocalDateTime startDateTime, LocalDateTime endDateTime, Member member, Pageable pageable) {
        QSchedule schedule = QSchedule.schedule;

        List<Schedule> results = queryFactory.selectFrom(schedule)
                .where(
                        schedule.startDateTime.between(startDateTime, endDateTime),
                        schedule.scheduleWriter.eq(member)
                )
                .orderBy(schedule.startDateTime.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory.selectFrom(schedule)
                .where(
                        schedule.startDateTime.between(startDateTime, endDateTime),
                        schedule.scheduleWriter.eq(member)
                )
                .fetchCount();

        return new PageImpl<>(results, pageable, total);
    }

    @Override
    public Page<Schedule> findActivitySchedulesByDateRangeAndMember(LocalDateTime startDateTime, LocalDateTime endDateTime, Member member, Pageable pageable) {
        QSchedule schedule = QSchedule.schedule;

        List<Schedule> results = queryFactory.selectFrom(schedule)
                .where(
                        schedule.startDateTime.between(startDateTime, endDateTime),
                        schedule.scheduleWriter.eq(member),
                        schedule.scheduleType.ne(ScheduleType.ALL)
                )
                .orderBy(schedule.startDateTime.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory.selectFrom(schedule)
                .where(
                        schedule.startDateTime.between(startDateTime, endDateTime),
                        schedule.scheduleWriter.eq(member),
                        schedule.scheduleType.ne(ScheduleType.ALL)
                )
                .fetchCount();

        return new PageImpl<>(results, pageable, total);
    }

}