package page.clab.api.domain.memberManagement.member.adapter.out.persistence;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import page.clab.api.domain.memberManagement.member.domain.Role;
import page.clab.api.global.util.OrderSpecifierUtil;

@Repository
@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<MemberJpaEntity> findByConditions(String id, String name, Pageable pageable) {
        QMemberJpaEntity member = QMemberJpaEntity.memberJpaEntity;
        BooleanBuilder builder = new BooleanBuilder();

        if (id != null) {
            builder.and(member.id.eq(id));
        }
        if (name != null) {
            builder.and(member.name.eq(name));
        }

        List<MemberJpaEntity> members = queryFactory
            .selectFrom(member)
            .where(builder)
            .orderBy(OrderSpecifierUtil.getOrderSpecifiers(pageable, member))
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        long totalCount = queryFactory.selectFrom(member)
            .where(builder)
            .fetchCount();

        return new PageImpl<>(members, pageable, totalCount);
    }

    @Override
    public Page<MemberJpaEntity> findBirthdaysThisMonth(int month, Pageable pageable) {
        QMemberJpaEntity qMember = QMemberJpaEntity.memberJpaEntity;

        List<OrderSpecifier<?>> orderSpecifiers = new ArrayList<>();
        for (Sort.Order order : pageable.getSort()) {
            String property = order.getProperty();
            Order direction = order.isAscending() ? Order.ASC : Order.DESC;
            if ("birth".equals(property)) {
                OrderSpecifier<?> dayOrderSpecifier = new OrderSpecifier<>(
                    direction,
                    Expressions.numberTemplate(Integer.class, "day({0})", qMember.birth)
                );
                orderSpecifiers.add(dayOrderSpecifier);
            } else {
                PathBuilder<Object> path = new PathBuilder<>(qMember.getType(), qMember.getMetadata());
                orderSpecifiers.add(new OrderSpecifier(direction, path.get(property)));
            }
        }

        List<MemberJpaEntity> members = queryFactory
            .selectFrom(qMember)
            .where(
                birthdayInMonth(month)
                    .and(qMember.name.ne("관리자"))
                    .and(qMember.role.ne(Role.GUEST))
            )            .orderBy(orderSpecifiers.toArray(new OrderSpecifier[0]))
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        long total = queryFactory
            .selectFrom(qMember)
            .where(birthdayInMonth(month))
            .fetchCount();

        return new PageImpl<>(members, pageable, total);
    }

    @Override
    public Page<MemberJpaEntity> findMemberRoleInfoByConditions(String memberId, String memberName, Role role,
        Pageable pageable) {
        QMemberJpaEntity member = QMemberJpaEntity.memberJpaEntity;
        BooleanBuilder builder = new BooleanBuilder();

        if (memberId != null) {
            builder.and(member.id.eq(memberId));
        }
        if (memberName != null) {
            builder.and(member.name.eq(memberName));
        }
        if (role != null) {
            builder.and(member.role.eq(role));
        }

        List<MemberJpaEntity> members = queryFactory
            .selectFrom(member)
            .where(builder)
            .orderBy(OrderSpecifierUtil.getOrderSpecifiers(pageable, member))
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        long totalCount = queryFactory.selectFrom(member)
            .where(builder)
            .fetchCount();

        return new PageImpl<>(members, pageable, totalCount);
    }

    private BooleanExpression birthdayInMonth(int month) {
        return QMemberJpaEntity.memberJpaEntity.birth.month().eq(month);
    }
}
