package page.clab.api.domain.activityGroup.dao;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import page.clab.api.domain.activityGroup.domain.ActivityGroup;
import page.clab.api.domain.activityGroup.domain.ActivityGroupBoard;
import page.clab.api.domain.activityGroup.domain.ActivityGroupBoardCategory;
import page.clab.api.domain.activityGroup.domain.GroupMember;
import page.clab.api.domain.activityGroup.domain.QActivityGroup;
import page.clab.api.domain.activityGroup.domain.QActivityGroupBoard;
import page.clab.api.domain.activityGroup.domain.QGroupMember;
import page.clab.api.domain.activityGroup.dto.param.ActivityGroupDetails;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ActivityGroupDetailsRepositoryImpl implements ActivityGroupDetailsRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public ActivityGroupDetails fetchActivityGroupDetails(Long activityGroupId) {
        QActivityGroup activityGroup = QActivityGroup.activityGroup;
        QGroupMember groupMember = QGroupMember.groupMember;
        QActivityGroupBoard activityGroupBoard = QActivityGroupBoard.activityGroupBoard;

        List<GroupMember> groupMembers = queryFactory.selectFrom(groupMember)
                .where(groupMember.activityGroup.id.eq(activityGroupId))
                .fetch();

        List<ActivityGroupBoard> boards = queryFactory.selectFrom(activityGroupBoard)
                .where(activityGroupBoard.activityGroup.id.eq(activityGroupId),
                        activityGroupBoard.category.in(ActivityGroupBoardCategory.NOTICE, ActivityGroupBoardCategory.WEEKLY_ACTIVITY, ActivityGroupBoardCategory.ASSIGNMENT))
                .orderBy(activityGroupBoard.createdAt.desc())
                .fetch();

        ActivityGroup foundActivityGroup = queryFactory.selectFrom(activityGroup)
                .where(activityGroup.id.eq(activityGroupId))
                .fetchOne();

        return new ActivityGroupDetails(foundActivityGroup, groupMembers, boards);
    }

}
