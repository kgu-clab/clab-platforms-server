package page.clab.api.domain.activity.review.adapter.out.persistence;

import org.springframework.stereotype.Component;
import page.clab.api.domain.activity.review.domain.Review;

@Component
public class ReviewMapper {

    public ReviewJpaEntity toJpaEntity(Review review) {
        return ReviewJpaEntity.builder()
                .id(review.getId())
                .activityGroup(review.getActivityGroup())
                .memberId(review.getMemberId())
                .content(review.getContent())
                .isPublic(review.getIsPublic())
                .isDeleted(review.isDeleted())
                .build();
    }

    public Review toDomainEntity(ReviewJpaEntity entity) {
        return Review.builder()
                .id(entity.getId())
                .activityGroup(entity.getActivityGroup())
                .memberId(entity.getMemberId())
                .content(entity.getContent())
                .isPublic(entity.getIsPublic())
                .isDeleted(entity.isDeleted())
                .createdAt(entity.getCreatedAt())
                .build();
    }
}
