package page.clab.api.domain.membershipFee.dao;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import page.clab.api.domain.member.domain.QMember;
import page.clab.api.domain.membershipFee.domain.MembershipFee;
import page.clab.api.domain.membershipFee.domain.MembershipFeeStatus;
import page.clab.api.domain.membershipFee.domain.QMembershipFee;
import page.clab.api.global.util.OrderSpecifierUtil;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class MembershipFeeRepositoryImpl implements MembershipFeeRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<MembershipFee> findByConditions(String memberId, String memberName, String category, MembershipFeeStatus status, Pageable pageable) {
        QMembershipFee membershipFee = QMembershipFee.membershipFee;
        QMember member = QMember.member;
        BooleanBuilder builder = new BooleanBuilder();

        if (memberId != null && !memberId.isEmpty()) builder.and(membershipFee.memberId.eq(memberId));
        if (memberName != null && !memberName.isEmpty()) builder.and(member.name.eq(memberName));
        if (category != null && !category.isEmpty()) builder.and(membershipFee.category.eq(category));
        if (status != null) builder.and(membershipFee.status.eq(status));

        List<MembershipFee> membershipFees = queryFactory.selectFrom(membershipFee)
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