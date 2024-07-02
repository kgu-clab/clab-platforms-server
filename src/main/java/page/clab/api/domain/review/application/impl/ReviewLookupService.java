package page.clab.api.domain.review.application.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import page.clab.api.domain.review.application.ReviewLookupUseCase;
import page.clab.api.domain.review.dao.ReviewRepository;
import page.clab.api.domain.review.domain.Review;
import page.clab.api.global.exception.NotFoundException;

@Service
@RequiredArgsConstructor
public class ReviewLookupService implements ReviewLookupUseCase {

    private final ReviewRepository reviewRepository;

    @Override
    public Review getReviewByIdOrThrow(Long reviewId) {
        return reviewRepository.findById(reviewId)
                .orElseThrow(() -> new NotFoundException("[Review] id: " + reviewId + "에 해당하는 리뷰가 존재하지 않습니다."));
    }
}
