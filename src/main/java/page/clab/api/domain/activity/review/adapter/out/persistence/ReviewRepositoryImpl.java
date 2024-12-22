package page.clab.api.domain.activity.review.adapter.out.persistence;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import page.clab.api.domain.memberManagement.member.adapter.out.persistence.QMemberJpaEntity;
import page.clab.api.global.util.OrderSpecifierUtil;

@Repository
@RequiredArgsConstructor
public class ReviewRepositoryImpl implements ReviewRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<ReviewJpaEntity> findByConditions(String memberId, String memberName, Long activityId, Boolean isPublic,
        Pageable pageable) {
        QReviewJpaEntity review = QReviewJpaEntity.reviewJpaEntity;
        BooleanBuilder builder = new BooleanBuilder();

        if (memberId != null) {
            builder.and(review.memberId.eq(memberId));
        }
        if (memberName != null && !memberName.isBlank()) {
            QMemberJpaEntity member = QMemberJpaEntity.memberJpaEntity;
            builder.and(JPAExpressions.selectFrom(member)
                .where(member.name.eq(memberName)
                    .and(member.id.eq(review.memberId)))
                .exists());
        }
        if (activityId != null) {
            builder.and(review.activityGroup.id.eq(activityId));
        }
        if (isPublic != null) {
            builder.and(review.isPublic.eq(isPublic));
        }

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
