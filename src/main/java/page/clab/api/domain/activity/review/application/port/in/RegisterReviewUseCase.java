package page.clab.api.domain.activity.review.application.port.in;

import page.clab.api.domain.activity.review.application.dto.request.ReviewRequestDto;

public interface RegisterReviewUseCase {

    Long registerReview(ReviewRequestDto requestDto);
}
