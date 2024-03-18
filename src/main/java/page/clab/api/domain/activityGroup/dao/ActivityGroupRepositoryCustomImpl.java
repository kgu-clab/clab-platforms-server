package page.clab.api.domain.activityGroup.dao;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import page.clab.api.domain.activityGroup.domain.ActivityGroup;
import page.clab.api.domain.activityGroup.domain.ActivityGroupStatus;
import page.clab.api.domain.activityGroup.domain.QActivityGroup;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ActivityGroupRepositoryCustomImpl implements ActivityGroupRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<ActivityGroup> findActivityGroupsByStatus(ActivityGroupStatus status) {
        QActivityGroup qActivityGroup = QActivityGroup.activityGroup;
        return queryFactory.selectFrom(qActivityGroup)
                .where(qActivityGroup.status.eq(status))
                .fetch();
    }

}
