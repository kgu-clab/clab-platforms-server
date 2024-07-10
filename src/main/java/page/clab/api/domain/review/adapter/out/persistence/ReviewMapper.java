package page.clab.api.domain.review.adapter.out.persistence;

import org.springframework.stereotype.Component;
import page.clab.api.domain.review.domain.Review;

@Component
public class ReviewMapper {

    public ReviewJpaEntity toJpaEntity(Review review) {
        return ReviewJpaEntity.builder()
                .id(review.getId())
                .activityGroup(review.getActivityGroup())
                .member(review.getMember())
                .content(review.getContent())
                .isPublic(review.getIsPublic())
                .isDeleted(review.isDeleted())
                .build();
    }

    public Review toDomainEntity(ReviewJpaEntity entity) {
        return Review.builder()
                .id(entity.getId())
                .activityGroup(entity.getActivityGroup())
                .member(entity.getMember())
                .content(entity.getContent())
                .isPublic(entity.getIsPublic())
                .isDeleted(entity.isDeleted())
                .createdAt(entity.getCreatedAt())
                .build();
    }
}
