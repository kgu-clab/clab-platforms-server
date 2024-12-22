package page.clab.api.domain.members.donation.adapter.out.persistence;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import page.clab.api.domain.memberManagement.member.adapter.out.persistence.QMemberJpaEntity;
import page.clab.api.global.util.OrderSpecifierUtil;

@Repository
@RequiredArgsConstructor
public class DonationRepositoryImpl implements DonationRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<DonationJpaEntity> findByConditions(String memberId, String name, LocalDate startDate,
        LocalDate endDate, Pageable pageable) {
        QDonationJpaEntity donation = QDonationJpaEntity.donationJpaEntity;
        QMemberJpaEntity member = QMemberJpaEntity.memberJpaEntity;

        BooleanBuilder builder = new BooleanBuilder();
        if (memberId != null && !memberId.isBlank()) {
            builder.and(donation.memberId.eq(memberId));
        }
        if (name != null && !name.isBlank()) {
            builder.and(member.name.containsIgnoreCase(name));
        }
        if (startDate != null) {
            builder.and(donation.createdAt.goe(startDate.atStartOfDay()));
        }
        if (endDate != null) {
            builder.and(donation.createdAt.loe(endDate.plusDays(1).atStartOfDay()));
        }

        List<DonationJpaEntity> results = queryFactory
            .selectFrom(donation)
            .leftJoin(member).on(donation.memberId.eq(member.id))
            .where(builder)
            .orderBy(OrderSpecifierUtil.getOrderSpecifiers(pageable, donation))
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        long count = queryFactory
            .selectFrom(donation)
            .leftJoin(member).on(donation.memberId.eq(member.id))
            .where(builder)
            .fetchCount();

        return new PageImpl<>(results, pageable, count);
    }
}
