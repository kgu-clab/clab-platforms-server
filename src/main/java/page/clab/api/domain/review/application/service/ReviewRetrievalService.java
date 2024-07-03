package page.clab.api.domain.review.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import page.clab.api.domain.review.application.port.in.RetrieveReviewUseCase;
import page.clab.api.domain.review.application.port.out.LoadReviewPort;
import page.clab.api.domain.review.domain.Review;

@Service
@RequiredArgsConstructor
public class ReviewRetrievalService implements RetrieveReviewUseCase {

    private final LoadReviewPort loadReviewPort;

    @Override
    public Review findByIdOrThrow(Long reviewId) {
        return loadReviewPort.findByIdOrThrow(reviewId);
    }
}
