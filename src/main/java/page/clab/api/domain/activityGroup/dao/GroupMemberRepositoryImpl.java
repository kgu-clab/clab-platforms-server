package page.clab.api.domain.activityGroup.dao;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import page.clab.api.domain.activityGroup.domain.ActivityGroupRole;
import page.clab.api.domain.activityGroup.domain.GroupMember;
import page.clab.api.domain.activityGroup.domain.GroupMemberStatus;
import page.clab.api.domain.activityGroup.domain.QGroupMember;

@Repository
@RequiredArgsConstructor
public class GroupMemberRepositoryImpl implements GroupMemberRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public long countAcceptedMembersByActivityGroupId(Long activityGroupId) {
        QGroupMember qGroupMember = QGroupMember.groupMember;
        return queryFactory.selectFrom(qGroupMember)
                .where(qGroupMember.activityGroup.id.eq(activityGroupId)
                        .and(qGroupMember.status.eq(GroupMemberStatus.ACCEPTED)))
                .fetchCount();
    }

    @Override
    public GroupMember findLeaderByActivityGroupId(Long activityGroupId) {
        QGroupMember qGroupMember = QGroupMember.groupMember;
        return queryFactory.selectFrom(qGroupMember)
                .where(qGroupMember.activityGroup.id.eq(activityGroupId)
                        .and(qGroupMember.role.eq(ActivityGroupRole.LEADER)))
                .fetchOne();
    }
}
