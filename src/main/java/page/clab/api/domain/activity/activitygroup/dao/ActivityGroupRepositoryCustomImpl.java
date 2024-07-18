package page.clab.api.domain.activity.activitygroup.dao;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import page.clab.api.domain.activity.activitygroup.domain.ActivityGroup;
import page.clab.api.domain.activity.activitygroup.domain.ActivityGroupStatus;
import page.clab.api.domain.activity.activitygroup.domain.QActivityGroup;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ActivityGroupRepositoryCustomImpl implements ActivityGroupRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<ActivityGroup> findActivityGroupsByStatus(ActivityGroupStatus status) {
        QActivityGroup qActivityGroup = QActivityGroup.activityGroup;
        BooleanBuilder builder = new BooleanBuilder();

        if (status != null) builder.and(qActivityGroup.status.eq(status));

        return queryFactory.selectFrom(qActivityGroup)
                .where(builder)
                .fetch();
    }
}
