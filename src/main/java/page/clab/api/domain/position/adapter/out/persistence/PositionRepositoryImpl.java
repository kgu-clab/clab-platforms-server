package page.clab.api.domain.position.adapter.out.persistence;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import page.clab.api.domain.position.domain.PositionType;
import page.clab.api.global.util.OrderSpecifierUtil;

import java.util.List;

@Repository
@RequiredArgsConstructor
@Slf4j
public class PositionRepositoryImpl implements PositionRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<PositionJpaEntity> findByConditions(String year, PositionType positionType, Pageable pageable) {
        QPositionJpaEntity position = QPositionJpaEntity.positionJpaEntity;
        BooleanBuilder builder = new BooleanBuilder();

        if (year != null) builder.and(position.year.eq(year));
        if (positionType != null) builder.and(position.positionType.eq(positionType));

        List<PositionJpaEntity> positions = queryFactory.selectFrom(position)
                .where(builder)
                .orderBy(OrderSpecifierUtil.getOrderSpecifiers(pageable, position))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long count = queryFactory.selectFrom(position)
                .where(builder)
                .fetchCount();

        return new PageImpl<>(positions, pageable, count);
    }
}
