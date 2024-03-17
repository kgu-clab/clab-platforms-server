package page.clab.api.domain.review.dao;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import page.clab.api.domain.review.domain.QReview;
import page.clab.api.domain.review.domain.Review;

import java.util.List;

public class ReviewRepositoryImpl implements ReviewRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public ReviewRepositoryImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

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
                .orderBy(qReview.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long count = queryFactory.selectFrom(qReview)
                .where(builder)
                .fetchCount();

        return new PageImpl<>(reviews, pageable, count);
    }

}