package page.clab.api.domain.activity.activitygroup.dao;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import page.clab.api.domain.activity.activitygroup.domain.ActivityGroupBoard;
import page.clab.api.domain.activity.activitygroup.domain.ActivityGroupBoardCategory;
import page.clab.api.domain.activity.activitygroup.domain.QActivityGroupBoard;

@Repository
@RequiredArgsConstructor
public class ActivityGroupBoardRepositoryImpl implements ActivityGroupBoardRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<ActivityGroupBoard> findMySubmissionsWithFeedbacks(Long parentId, String memberId) {
        QActivityGroupBoard qBoard = QActivityGroupBoard.activityGroupBoard;
        QActivityGroupBoard qChild = new QActivityGroupBoard("child");

        BooleanBuilder builder = new BooleanBuilder();
        builder.and(qBoard.parent.id.eq(parentId));
        builder.and(qBoard.memberId.eq(memberId));
        builder.and(qBoard.category.eq(ActivityGroupBoardCategory.SUBMIT));

        return queryFactory.selectFrom(qBoard)
            .leftJoin(qBoard.children, qChild).fetchJoin()
            .where(builder)
            .distinct()
            .fetch();
    }
}
