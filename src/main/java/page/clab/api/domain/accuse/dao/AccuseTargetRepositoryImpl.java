package page.clab.api.domain.accuse.dao;

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

import java.util.List;
import page.clab.api.global.util.OrderSpecifierUtil;

@Repository
@RequiredArgsConstructor
public class AccuseTargetRepositoryImpl implements AccuseTargetRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<AccuseTarget> findByConditions(TargetType type, AccuseStatus status, boolean countOrder, Pageable pageable) {
        QAccuseTarget qAccuseTarget = QAccuseTarget.accuseTarget;
        BooleanExpression predicate = qAccuseTarget.isNotNull();

        if (type != null) {
            predicate = predicate.and(qAccuseTarget.targetType.eq(type));
        }
        if (status != null) {
            predicate = predicate.and(qAccuseTarget.accuseStatus.eq(status));
        }

        List<AccuseTarget> accuseTargets = queryFactory.selectFrom(qAccuseTarget)
                .where(predicate)
                .orderBy(OrderSpecifierUtil.getOrderSpecifiers(pageable, qAccuseTarget))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory.selectFrom(qAccuseTarget)
                .where(predicate)
                .fetchCount();

        return new PageImpl<>(accuseTargets, pageable, total);
    }
}