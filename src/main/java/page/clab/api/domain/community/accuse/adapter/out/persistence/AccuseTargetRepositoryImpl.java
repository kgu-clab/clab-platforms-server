package page.clab.api.domain.community.accuse.adapter.out.persistence;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import page.clab.api.domain.community.accuse.domain.AccuseStatus;
import page.clab.api.domain.community.accuse.domain.TargetType;
import page.clab.api.global.util.OrderSpecifierUtil;

@Repository
@RequiredArgsConstructor
public class AccuseTargetRepositoryImpl implements AccuseTargetRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<AccuseTargetJpaEntity> findByConditions(TargetType type, AccuseStatus status, boolean countOrder,
        Pageable pageable) {
        QAccuseTargetJpaEntity accuseTarget = QAccuseTargetJpaEntity.accuseTargetJpaEntity;
        BooleanExpression predicate = accuseTarget.isNotNull();

        if (type != null) {
            predicate = predicate.and(accuseTarget.targetType.eq(type));
        }
        if (status != null) {
            predicate = predicate.and(accuseTarget.accuseStatus.eq(status));
        }

        List<AccuseTargetJpaEntity> accuseTargets = queryFactory.selectFrom(accuseTarget)
            .where(predicate)
            .orderBy(OrderSpecifierUtil.getOrderSpecifiers(pageable, accuseTarget))
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        long total = queryFactory.selectFrom(accuseTarget)
            .where(predicate)
            .fetchCount();

        return new PageImpl<>(accuseTargets, pageable, total);
    }
}
