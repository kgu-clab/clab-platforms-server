package page.clab.api.domain.award.adapter.out.persistence;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import page.clab.api.domain.member.adapter.out.persistence.QMemberJpaEntity;
import page.clab.api.global.util.OrderSpecifierUtil;

import java.time.LocalDate;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class AwardRepositoryImpl implements AwardRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<AwardJpaEntity> findByConditions(String memberId, Long year, Pageable pageable) {
        QAwardJpaEntity award = QAwardJpaEntity.awardJpaEntity;
        QMemberJpaEntity member = QMemberJpaEntity.memberJpaEntity;

        BooleanBuilder builder = new BooleanBuilder();
        if (memberId != null) builder.and(award.memberId.eq(memberId));
        if (year != null) {
            LocalDate startOfYear = LocalDate.of(year.intValue(), 1, 1);
            LocalDate endOfYear = LocalDate.of(year.intValue(), 12, 31);
            builder.and(award.awardDate.between(startOfYear, endOfYear));
        }

        List<AwardJpaEntity> awards = queryFactory.selectFrom(award)
                .leftJoin(member).on(award.memberId.eq(member.id))
                .where(builder)
                .orderBy(OrderSpecifierUtil.getOrderSpecifiers(pageable, award))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long count = queryFactory
                .selectFrom(award)
                .where(builder)
                .fetchCount();

        return new PageImpl<>(awards, pageable, count);
    }
}
