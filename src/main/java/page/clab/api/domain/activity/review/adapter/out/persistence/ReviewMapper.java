package page.clab.api.domain.activity.review.adapter.out.persistence;

import org.mapstruct.Mapper;
import page.clab.api.domain.activity.review.domain.Review;

@Mapper(componentModel = "spring")
public interface ReviewMapper {

    ReviewJpaEntity toJpaEntity(Review review);

    Review toDomainEntity(ReviewJpaEntity entity);
}
