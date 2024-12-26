package page.clab.api.domain.members.membershipFee.adapter.out.persistence;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import page.clab.api.domain.memberManagement.member.adapter.out.persistence.QMemberJpaEntity;
import page.clab.api.domain.members.membershipFee.domain.MembershipFeeStatus;
import page.clab.api.global.util.OrderSpecifierUtil;

@Repository
@RequiredArgsConstructor
public class MembershipFeeRepositoryImpl implements MembershipFeeRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<MembershipFeeJpaEntity> findByConditions(String memberId, String memberName, String category,
        MembershipFeeStatus status, Pageable pageable) {
        QMembershipFeeJpaEntity membershipFee = QMembershipFeeJpaEntity.membershipFeeJpaEntity;
        QMemberJpaEntity member = QMemberJpaEntity.memberJpaEntity;
        BooleanBuilder builder = new BooleanBuilder();

        if (memberId != null && !memberId.isEmpty()) {
            builder.and(membershipFee.memberId.eq(memberId));
        }
        if (memberName != null && !memberName.isEmpty()) {
            builder.and(member.name.eq(memberName));
        }
        if (category != null && !category.isEmpty()) {
            builder.and(membershipFee.category.eq(category));
        }
        if (status != null) {
            builder.and(membershipFee.status.eq(status));
        }

        List<MembershipFeeJpaEntity> membershipFees = queryFactory.selectFrom(membershipFee)
            .leftJoin(member).on(membershipFee.memberId.eq(member.id))
            .where(builder)
            .orderBy(OrderSpecifierUtil.getOrderSpecifiers(pageable, membershipFee))
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        long count = queryFactory.selectFrom(membershipFee)
            .leftJoin(member).on(membershipFee.memberId.eq(member.id))
            .where(builder)
            .fetchCount();

        return new PageImpl<>(membershipFees, pageable, count);
    }
}
