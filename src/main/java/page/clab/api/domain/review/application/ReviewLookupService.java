package page.clab.api.domain.review.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import page.clab.api.domain.review.application.port.in.ReviewLookupUseCase;
import page.clab.api.domain.review.application.port.out.LoadReviewPort;
import page.clab.api.domain.review.domain.Review;

@Service
@RequiredArgsConstructor
public class ReviewLookupService implements ReviewLookupUseCase {

    private final LoadReviewPort loadReviewPort;

    @Override
    public Review getReviewByIdOrThrow(Long reviewId) {
        return loadReviewPort.findByIdOrThrow(reviewId);
    }
}
