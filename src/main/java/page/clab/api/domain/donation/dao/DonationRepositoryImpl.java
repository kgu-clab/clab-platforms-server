package page.clab.api.domain.donation.dao;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import page.clab.api.domain.donation.domain.Donation;
import page.clab.api.domain.donation.domain.QDonation;
import page.clab.api.domain.member.domain.QMember;

import java.time.LocalDate;
import java.util.List;

public class DonationRepositoryImpl implements DonationRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public DonationRepositoryImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public Page<Donation> findByConditions(String memberId, String name, LocalDate startDate, LocalDate endDate, Pageable pageable) {
        QDonation donation = QDonation.donation;
        QMember member = QMember.member;

        BooleanBuilder builder = new BooleanBuilder();
        if (memberId != null && !memberId.isBlank()) builder.and(donation.donor.id.eq(memberId));
        if (name != null && !name.isBlank()) builder.and(member.name.containsIgnoreCase(name));
        if (startDate != null) builder.and(donation.createdAt.goe(startDate.atStartOfDay()));
        if (endDate != null) builder.and(donation.createdAt.loe(endDate.plusDays(1).atStartOfDay()));

        List<Donation> results = queryFactory
                .selectFrom(donation)
                .leftJoin(donation.donor, member)
                .where(builder)
                .orderBy(donation.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long count = queryFactory
                .selectFrom(donation)
                .leftJoin(donation.donor, member)
                .where(builder)
                .fetchCount();

        return new PageImpl<>(results, pageable, count);
    }

}
