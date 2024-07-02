package page.clab.api.domain.review.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import page.clab.api.domain.activityGroup.domain.ActivityGroup;
import page.clab.api.domain.member.domain.Member;
import page.clab.api.domain.review.application.port.out.RegisterReviewPort;
import page.clab.api.domain.review.application.port.out.RemoveReviewPort;
import page.clab.api.domain.review.application.port.out.RetrieveDeletedReviewsPort;
import page.clab.api.domain.review.application.port.out.RetrieveMyReviewsPort;
import page.clab.api.domain.review.application.port.out.RetrieveReviewByIdPort;
import page.clab.api.domain.review.application.port.out.RetrieveReviewsByConditionsPort;
import page.clab.api.domain.review.application.port.out.UpdateReviewPort;
import page.clab.api.domain.review.domain.Review;
import page.clab.api.global.exception.NotFoundException;

@Component
@RequiredArgsConstructor
public class ReviewPersistenceAdapter implements
        RetrieveDeletedReviewsPort,
        RetrieveMyReviewsPort,
        RetrieveReviewByIdPort,
        RegisterReviewPort,
        RemoveReviewPort,
        RetrieveReviewsByConditionsPort,
        UpdateReviewPort {

    private final ReviewRepository repository;

    @Override
    public Page<Review> findAllByIsDeletedTrue(Pageable pageable) {
        return repository.findAllByIsDeletedTrue(pageable);
    }

    @Override
    public Page<Review> findAllByMember(Member member, Pageable pageable) {
        return repository.findAllByMember(member, pageable);
    }

    @Override
    public Review findByIdOrThrow(Long reviewId) {
        return repository.findById(reviewId)
                .orElseThrow(() -> new NotFoundException("[Review] id: " + reviewId + "에 해당하는 리뷰가 존재하지 않습니다."));
    }

    @Override
    public Review save(Review review) {
        return repository.save(review);
    }

    @Override
    public Page<Review> findByConditions(String memberId, String memberName, Long activityId, Boolean isPublic, Pageable pageable) {
        return repository.findByConditions(memberId, memberName, activityId, isPublic, pageable);
    }

    @Override
    public boolean existsByMemberAndActivityGroup(Member member, ActivityGroup activityGroup) {
        return repository.existsByMemberAndActivityGroup(member, activityGroup);
    }
}
