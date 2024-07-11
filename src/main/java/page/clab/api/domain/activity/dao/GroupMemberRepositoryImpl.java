package page.clab.api.domain.activity.dao;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import page.clab.api.domain.activity.domain.ActivityGroupRole;
import page.clab.api.domain.activity.domain.GroupMember;
import page.clab.api.domain.activity.domain.GroupMemberStatus;
import page.clab.api.domain.activity.domain.QGroupMember;

@Repository
@RequiredArgsConstructor
public class GroupMemberRepositoryImpl implements GroupMemberRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public long countAcceptedMembersByActivityGroupId(Long activityGroupId) {
        QGroupMember qGroupMember = QGroupMember.groupMember;
        BooleanBuilder builder = new BooleanBuilder();

        if (activityGroupId != null) builder.and(qGroupMember.activityGroup.id.eq(activityGroupId));
        builder.and(qGroupMember.status.eq(GroupMemberStatus.ACCEPTED));

        return queryFactory.selectFrom(qGroupMember)
                .where(builder)
                .fetchCount();
    }

    @Override
    public GroupMember findLeaderByActivityGroupId(Long activityGroupId) {
        QGroupMember qGroupMember = QGroupMember.groupMember;
        BooleanBuilder builder = new BooleanBuilder();

        if (activityGroupId != null) builder.and(qGroupMember.activityGroup.id.eq(activityGroupId));
        if (activityGroupId != null) builder.and(qGroupMember.role.eq(ActivityGroupRole.LEADER));

        return queryFactory.selectFrom(qGroupMember)
                .where(builder)
                .fetchOne();
    }
}
