package page.clab.api.domain.review.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import page.clab.api.domain.review.application.port.in.ReviewLookupUseCase;
import page.clab.api.domain.review.application.port.out.RetrieveReviewByIdPort;
import page.clab.api.domain.review.domain.Review;

@Service
@RequiredArgsConstructor
public class ReviewLookupService implements ReviewLookupUseCase {

    private final RetrieveReviewByIdPort retrieveReviewByIdPort;

    @Override
    public Review getReviewByIdOrThrow(Long reviewId) {
        return retrieveReviewByIdPort.findByIdOrThrow(reviewId);
    }
}
