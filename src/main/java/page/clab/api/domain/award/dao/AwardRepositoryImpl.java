package page.clab.api.domain.award.dao;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import page.clab.api.domain.award.domain.Award;
import page.clab.api.domain.award.domain.QAward;
import page.clab.api.domain.member.domain.QMember;

import java.time.LocalDate;
import java.util.List;
import page.clab.api.global.util.OrderSpecifierUtil;

@Repository
@RequiredArgsConstructor
public class AwardRepositoryImpl implements AwardRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Award> findByConditions(String memberId, Long year, Pageable pageable) {
        QAward qAward = QAward.award;
        QMember qMember = QMember.member;

        BooleanBuilder builder = new BooleanBuilder();
        if (memberId != null) builder.and(qAward.member.id.eq(memberId));
        if (year != null) {
            LocalDate startOfYear = LocalDate.of(year.intValue(), 1, 1);
            LocalDate endOfYear = LocalDate.of(year.intValue(), 12, 31);
            builder.and(qAward.awardDate.between(startOfYear, endOfYear));
        }

        List<Award> awards = queryFactory.selectFrom(qAward)
                .leftJoin(qAward.member, qMember)
                .where(builder)
                .orderBy(OrderSpecifierUtil.getOrderSpecifiers(pageable, qAward))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long count = queryFactory
                .selectFrom(qAward)
                .where(builder)
                .fetchCount();

        return new PageImpl<>(awards, pageable, count);
    }

}