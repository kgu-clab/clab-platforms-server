package page.clab.api.domain.accuse.dao;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import page.clab.api.domain.accuse.domain.Accuse;
import page.clab.api.domain.accuse.domain.AccuseStatus;
import page.clab.api.domain.accuse.domain.QAccuse;
import page.clab.api.domain.accuse.domain.TargetType;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class AccuseRepositoryImpl implements AccuseRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Accuse> findByConditions(TargetType targetType, AccuseStatus accuseStatus, Pageable pageable) {
        QAccuse accuse = QAccuse.accuse;
        BooleanBuilder builder = new BooleanBuilder();

        if (targetType != null) {
            builder.and(accuse.target.targetType.eq(targetType));
        }
        if (accuseStatus != null) {
            builder.and(accuse.accuseStatus.eq(accuseStatus));
        }

        List<Accuse> result = queryFactory.selectFrom(accuse)
                .where(builder)
                .orderBy(accuse.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory.selectFrom(accuse)
                .where(builder)
                .fetchCount();

        return new PageImpl<>(result, pageable, total);
    }

}