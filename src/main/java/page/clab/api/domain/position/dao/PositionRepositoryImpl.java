package page.clab.api.domain.position.dao;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.ArrayList;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import page.clab.api.domain.position.domain.Position;
import page.clab.api.domain.position.domain.PositionType;
import page.clab.api.domain.position.domain.QPosition;

import java.util.List;
import page.clab.api.global.util.OrderSpecifierUtil;

@Repository
@RequiredArgsConstructor
@Slf4j
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
                .orderBy(OrderSpecifierUtil.getOrderSpecifiers(pageable, qPosition))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long count = queryFactory.selectFrom(qPosition)
                .where(builder)
                .fetchCount();

        return new PageImpl<>(positions, pageable, count);
    }

}