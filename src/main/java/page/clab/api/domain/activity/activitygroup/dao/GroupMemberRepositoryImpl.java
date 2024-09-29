package page.clab.api.domain.activity.activitygroup.dao;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import page.clab.api.domain.activity.activitygroup.domain.*;
import page.clab.api.global.util.OrderSpecifierUtil;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

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
    public List<GroupMember> findLeaderByActivityGroupId(Long activityGroupId) {
        QGroupMember qGroupMember = QGroupMember.groupMember;
        BooleanBuilder builder = new BooleanBuilder();

        if (activityGroupId != null) builder.and(qGroupMember.activityGroup.id.eq(activityGroupId));
        if (activityGroupId != null) builder.and(qGroupMember.role.eq(ActivityGroupRole.LEADER));

        return queryFactory.selectFrom(qGroupMember)
                .where(builder)
                .fetch();
    }

    @Override
    public Page<GroupMember> findAllByActivityGroupId(Long activityGroupId, Pageable pageable) {
        QGroupMember qGroupMember = QGroupMember.groupMember;
        BooleanBuilder builder = new BooleanBuilder();

        if (activityGroupId != null) builder.and(qGroupMember.activityGroup.id.eq(activityGroupId));

        OrderSpecifier<Integer> leaderOrder = new CaseBuilder()
                .when(qGroupMember.role.eq(ActivityGroupRole.LEADER)).then(0)
                .otherwise(1)
                .asc();

        OrderSpecifier<Integer> statusOrder = pageable.getSort().stream()
                .filter(order -> order.getProperty().equals("status"))
                .findFirst()
                .map(order -> new CaseBuilder()
                        .when(qGroupMember.status.eq(GroupMemberStatus.WAITING)).then(0)
                        .when(qGroupMember.status.eq(GroupMemberStatus.ACCEPTED)).then(1)
                        .when(qGroupMember.status.eq(GroupMemberStatus.REJECTED)).then(2)
                        .otherwise(3)
                        .asc())
                .orElse(null);

        OrderSpecifier<?>[] dynamicOrderSpecifiers = OrderSpecifierUtil.getOrderSpecifiers(pageable, qGroupMember);

        List<GroupMember> groupMembers = queryFactory.selectFrom(qGroupMember)
                .where(builder)
                .orderBy(Stream.concat(
                        Stream.of(leaderOrder, statusOrder).filter(Objects::nonNull),
                        Arrays.stream(dynamicOrderSpecifiers)
                ).toArray(OrderSpecifier[]::new))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = queryFactory.select(qGroupMember.count())
                .from(qGroupMember)
                .where(builder)
                .fetchOne();

        long totalElements = total != null ? total : 0;

        return new PageImpl<>(groupMembers, pageable, totalElements);
    }
}
