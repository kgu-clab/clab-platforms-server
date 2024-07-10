package page.clab.api.domain.review.adapter.out.persistence;

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
public class ReviewRepositoryImpl implements ReviewRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<ReviewJpaEntity> findByConditions(String memberId, String memberName, Long activityId, Boolean isPublic, Pageable pageable) {
        QReviewJpaEntity review = QReviewJpaEntity.reviewJpaEntity;
        BooleanBuilder builder = new BooleanBuilder();

        if (memberId != null) builder.and(review.member.id.eq(memberId));
        if (memberName != null && !memberName.isBlank()) builder.and(review.member.name.eq(memberName));
        if (activityId != null) builder.and(review.activityGroup.id.eq(activityId));
        if (isPublic != null) builder.and(review.isPublic.eq(isPublic));

        List<ReviewJpaEntity> reviews = queryFactory.selectFrom(review)
                .where(builder)
                .orderBy(OrderSpecifierUtil.getOrderSpecifiers(pageable, review))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long count = queryFactory.selectFrom(review)
                .where(builder)
                .fetchCount();

        return new PageImpl<>(reviews, pageable, count);
    }
}
