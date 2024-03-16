package page.clab.api.domain.membershipFee.dao;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import page.clab.api.domain.member.domain.QMember;
import page.clab.api.domain.membershipFee.domain.MembershipFee;
import page.clab.api.domain.membershipFee.domain.QMembershipFee;

import java.util.List;

public class MembershipFeeRepositoryImpl implements MembershipFeeRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public MembershipFeeRepositoryImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public Page<MembershipFee> findByConditions(String memberId, String memberName, String category, Pageable pageable) {
        QMembershipFee qMembershipFee = QMembershipFee.membershipFee;
        QMember qMember = QMember.member;
        BooleanBuilder builder = new BooleanBuilder();

        if (memberId != null && !memberId.isEmpty()) builder.and(qMembershipFee.applicant.id.eq(memberId));
        if (memberName != null && !memberName.isEmpty()) builder.and(qMember.name.eq(memberName));
        if (category != null && !category.isEmpty()) builder.and(qMembershipFee.category.eq(category));

        List<MembershipFee> membershipFees = queryFactory.selectFrom(qMembershipFee)
                .leftJoin(qMembershipFee.applicant, qMember)
                .where(builder)
                .orderBy(qMembershipFee.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long count = queryFactory.selectFrom(qMembershipFee)
                .leftJoin(qMembershipFee.applicant, qMember)
                .where(builder)
                .fetchCount();

        return new PageImpl<>(membershipFees, pageable, count);
    }

}