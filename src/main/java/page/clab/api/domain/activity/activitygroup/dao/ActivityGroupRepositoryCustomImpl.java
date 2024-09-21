package page.clab.api.domain.activity.activitygroup.dao;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import page.clab.api.domain.activity.activitygroup.domain.ActivityGroup;
import page.clab.api.domain.activity.activitygroup.domain.ActivityGroupStatus;
import page.clab.api.domain.activity.activitygroup.domain.QActivityGroup;
import page.clab.api.global.util.OrderSpecifierUtil;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ActivityGroupRepositoryCustomImpl implements ActivityGroupRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<ActivityGroup> findActivityGroupsByStatus(ActivityGroupStatus status, Pageable pageable) {
        QActivityGroup qActivityGroup = QActivityGroup.activityGroup;
        BooleanBuilder builder = new BooleanBuilder();

        if (status != null) builder.and(qActivityGroup.status.eq(status));

        List<ActivityGroup> activityGroups = queryFactory.selectFrom(qActivityGroup)
                .where(builder)
                .orderBy(OrderSpecifierUtil.getOrderSpecifiers(pageable, qActivityGroup))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = queryFactory.select(qActivityGroup.count())
                .from(qActivityGroup)
                .where(builder)
                .fetchOne();

        long totalElements = total != null ? total : 0;

        return new PageImpl<>(activityGroups, pageable, totalElements);
    }
}
