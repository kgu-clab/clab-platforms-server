package page.clab.api.domain.review.adapter.out.persistence;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import page.clab.api.domain.activityGroup.domain.ActivityGroup;
import page.clab.api.domain.review.application.port.out.RegisterReviewPort;
import page.clab.api.domain.review.application.port.out.RetrieveReviewPort;
import page.clab.api.domain.review.application.port.out.UpdateReviewPort;
import page.clab.api.domain.review.domain.Review;
import page.clab.api.global.exception.NotFoundException;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ReviewPersistenceAdapter implements
        RegisterReviewPort,
        UpdateReviewPort,
        RetrieveReviewPort {

    private final ReviewRepository repository;
    private final ReviewMapper mapper;

    @Override
    public Review save(Review review) {
        ReviewJpaEntity entity = mapper.toJpaEntity(review);
        ReviewJpaEntity savedEntity = repository.save(entity);
        return mapper.toDomainEntity(savedEntity);
    }

    @Override
    public Review update(Review review) {
        ReviewJpaEntity entity = mapper.toJpaEntity(review);
        ReviewJpaEntity updatedEntity = repository.save(entity);
        return mapper.toDomainEntity(updatedEntity);
    }

    @Override
    public Optional<Review> findById(Long reviewId) {
        return repository.findById(reviewId)
                .map(mapper::toDomainEntity);
    }

    @Override
    public Review findByIdOrThrow(Long reviewId) {
        return repository.findById(reviewId)
                .map(mapper::toDomainEntity)
                .orElseThrow(() -> new NotFoundException("[Review] id: " + reviewId + "에 해당하는 리뷰가 존재하지 않습니다."));
    }

    @Override
    public Page<Review> findAllByIsDeletedTrue(Pageable pageable) {
        return repository.findAllByIsDeletedTrue(pageable)
                .map(mapper::toDomainEntity);
    }

    @Override
    public Page<Review> findAllByMemberId(String memberId, Pageable pageable) {
        return repository.findAllByMemberId(memberId, pageable)
                .map(mapper::toDomainEntity);
    }

    @Override
    public boolean existsByMemberIdAndActivityGroup(String memberId, ActivityGroup activityGroup) {
        return repository.existsByMemberIdAndActivityGroup(memberId, activityGroup);
    }

    @Override
    public Page<Review> findByConditions(String memberId, String memberName, Long activityId, Boolean isPublic, Pageable pageable) {
        return repository.findByConditions(memberId, memberName, activityId, isPublic, pageable)
                .map(mapper::toDomainEntity);
    }
}
