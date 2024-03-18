package page.clab.api.domain.position.dao;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import page.clab.api.domain.position.domain.Position;
import page.clab.api.domain.position.domain.PositionType;
import page.clab.api.domain.position.domain.QPosition;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class PositionRepositoryImpl implements PositionRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Position> findByConditions(String year, PositionType positionType, Pageable pageable) {
        QPosition qPosition = QPosition.position;
        BooleanBuilder builder = new BooleanBuilder();

        if (year != null) builder.and(qPosition.year.eq(year));
        if (positionType != null) builder.and(qPosition.positionType.eq(positionType));

        List<Position> positions = queryFactory.selectFrom(qPosition)
                .where(builder)
                .orderBy(qPosition.year.desc(), qPosition.positionType.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long count = queryFactory.selectFrom(qPosition)
                .where(builder)
                .fetchCount();

        return new PageImpl<>(positions, pageable, count);
    }

}