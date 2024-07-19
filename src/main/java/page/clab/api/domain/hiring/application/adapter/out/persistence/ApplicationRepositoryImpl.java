package page.clab.api.domain.hiring.application.adapter.out.persistence;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import page.clab.api.global.util.OrderSpecifierUtil;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ApplicationRepositoryImpl implements ApplicationRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<ApplicationJpaEntity> findByConditions(Long recruitmentId, String studentId, Boolean isPass, Pageable pageable) {
        QApplicationJpaEntity application = QApplicationJpaEntity.applicationJpaEntity;
        BooleanBuilder builder = new BooleanBuilder();

        if (recruitmentId != null) builder.and(application.recruitmentId.eq(recruitmentId));
        if (studentId != null && !studentId.isEmpty()) builder.and(application.studentId.eq(studentId));
        if (isPass != null) builder.and(application.isPass.eq(isPass));

        List<ApplicationJpaEntity> applications = queryFactory.selectFrom(application)
                .where(builder.getValue())
                .orderBy(OrderSpecifierUtil.getOrderSpecifiers(pageable, application))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long count = queryFactory.from(application)
                .where(builder)
                .fetchCount();

        return new PageImpl<>(applications, pageable, count);
    }
}
