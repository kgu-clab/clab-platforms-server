package page.clab.api.domain.schedule.dao;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.ArrayList;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import page.clab.api.domain.member.domain.Member;
import page.clab.api.domain.schedule.domain.QSchedule;
import page.clab.api.domain.schedule.domain.Schedule;
import page.clab.api.domain.schedule.domain.SchedulePriority;
import page.clab.api.domain.schedule.domain.ScheduleType;
import page.clab.api.domain.schedule.dto.response.ScheduleCollectResponseDto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import page.clab.api.global.util.OrderSpecifierUtil;

@Repository
@RequiredArgsConstructor
public class ScheduleRepositoryImpl implements ScheduleRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Schedule> findByDateRangeAndMember(LocalDate startDate, LocalDate endDate, Member member, Pageable pageable) {
        QSchedule schedule = QSchedule.schedule;
        BooleanBuilder builder = new BooleanBuilder();

        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(23, 59, 59);

        builder.and(schedule.endDateTime.goe(startDateTime))
                .and(schedule.startDateTime.loe(endDateTime))
                .and(schedule.scheduleWriter.eq(member));

        List<Schedule> results = queryFactory.selectFrom(schedule)
                .where(builder)
                .orderBy(OrderSpecifierUtil.getOrderSpecifiers(pageable, "schedule"))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory.selectFrom(schedule)
                .where(builder)
                .fetchCount();

        return new PageImpl<>(results, pageable, total);
    }

    @Override
    public Page<Schedule> findByConditions(Integer year, Integer month, SchedulePriority priority, Pageable pageable) {
        QSchedule schedule = QSchedule.schedule;
        BooleanBuilder builder = new BooleanBuilder();

        if (year != null) {
            builder.and(schedule.startDateTime.year().eq(year));
        }
        if (month != null) {
            builder.and(schedule.startDateTime.month().eq(month));
        }
        if (priority != null) {
            builder.and(schedule.priority.eq(priority));
        }

        List<Schedule> results = queryFactory.selectFrom(schedule)
                .where(builder)
                .orderBy(OrderSpecifierUtil.getOrderSpecifiers(pageable, "schedule"))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory.selectFrom(schedule)
                .where(builder)
                .fetchCount();

        return new PageImpl<>(results, pageable, total);
    }

    @Override
    public Page<Schedule> findActivitySchedulesByDateRangeAndMember(LocalDate startDate, LocalDate endDate, Member member, Pageable pageable) {
        QSchedule schedule = QSchedule.schedule;
        BooleanBuilder builder = new BooleanBuilder();

        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(23, 59, 59);

        builder.and(schedule.endDateTime.goe(startDateTime))
                .and(schedule.startDateTime.loe(endDateTime))
                .and(schedule.scheduleWriter.eq(member))
                .and(schedule.scheduleType.ne(ScheduleType.ALL));

        List<Schedule> results = queryFactory.selectFrom(schedule)
                .where(builder)
                .orderBy(OrderSpecifierUtil.getOrderSpecifiers(pageable, "schedule"))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory.selectFrom(schedule)
                .where(builder)
                .fetchCount();

        return new PageImpl<>(results, pageable, total);
    }

    @Override
    public ScheduleCollectResponseDto findCollectSchedules() {
        QSchedule schedule = QSchedule.schedule;
        BooleanBuilder builder = new BooleanBuilder();

        LocalDateTime startDateTime = LocalDate.now().withDayOfYear(1).atStartOfDay();
        LocalDateTime endDateTime = LocalDate.now().withDayOfYear(LocalDate.now().lengthOfYear()).atTime(23, 59, 59);

        builder.and(schedule.startDateTime.goe(startDateTime))
                .and(schedule.endDateTime.loe(endDateTime));

        long total = queryFactory.selectFrom(schedule)
                .where(builder)
                .fetchCount();

        builder.and(schedule.priority.eq(SchedulePriority.HIGH));

        long highPriorityCount = queryFactory.selectFrom(schedule)
                .where(builder)
                .fetchCount();

        return ScheduleCollectResponseDto.toDto(total, highPriorityCount);
    }

}