package page.clab.api.domain.activity.review.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import page.clab.api.domain.activity.review.application.port.in.RetrieveReviewUseCase;
import page.clab.api.domain.activity.review.application.port.out.RetrieveReviewPort;
import page.clab.api.domain.activity.review.domain.Review;

@Service
@RequiredArgsConstructor
public class ReviewRetrievalService implements RetrieveReviewUseCase {

    private final RetrieveReviewPort retrieveReviewPort;

    @Override
    public Review findByIdOrThrow(Long reviewId) {
        return retrieveReviewPort.findByIdOrThrow(reviewId);
    }
}
