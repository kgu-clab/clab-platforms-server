package page.clab.api.domain.accuse.adapter.out.persistence;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import page.clab.api.domain.accuse.domain.AccuseStatus;
import page.clab.api.domain.accuse.domain.AccuseTarget;
import page.clab.api.domain.accuse.domain.QAccuseTarget;
import page.clab.api.domain.accuse.domain.TargetType;
import page.clab.api.global.util.OrderSpecifierUtil;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class AccuseTargetRepositoryImpl implements AccuseTargetRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<AccuseTarget> findByConditions(TargetType type, AccuseStatus status, boolean countOrder, Pageable pageable) {
        QAccuseTarget accuseTarget = QAccuseTarget.accuseTarget;
        BooleanExpression predicate = accuseTarget.isNotNull();

        if (type != null) {
            predicate = predicate.and(accuseTarget.targetType.eq(type));
        }
        if (status != null) {
            predicate = predicate.and(accuseTarget.accuseStatus.eq(status));
        }

        List<AccuseTarget> accuseTargets = queryFactory.selectFrom(accuseTarget)
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
