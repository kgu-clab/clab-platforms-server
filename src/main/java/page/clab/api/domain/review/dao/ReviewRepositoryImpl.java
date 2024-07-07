package page.clab.api.domain.review.dao;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import page.clab.api.domain.review.domain.QReview;
import page.clab.api.domain.review.domain.Review;
import page.clab.api.global.util.OrderSpecifierUtil;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ReviewRepositoryImpl implements ReviewRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Review> findByConditions(String memberId, String memberName, Long activityId, Boolean isPublic, Pageable pageable) {
        QReview qReview = QReview.review;
        BooleanBuilder builder = new BooleanBuilder();

        if (memberId != null) builder.and(qReview.member.id.eq(memberId));
        if (memberName != null && !memberName.isBlank()) builder.and(qReview.member.name.eq(memberName));
        if (activityId != null) builder.and(qReview.activityGroup.id.eq(activityId));
        if (isPublic != null) builder.and(qReview.isPublic.eq(isPublic));

        List<Review> reviews = queryFactory.selectFrom(qReview)
                .where(builder)
                .orderBy(OrderSpecifierUtil.getOrderSpecifiers(pageable, qReview))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long count = queryFactory.selectFrom(qReview)
                .where(builder)
                .fetchCount();

        return new PageImpl<>(reviews, pageable, count);
    }
}