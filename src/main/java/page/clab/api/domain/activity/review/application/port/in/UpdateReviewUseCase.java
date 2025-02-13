package page.clab.api.domain.activity.review.application.port.in;

import page.clab.api.domain.activity.review.application.dto.request.ReviewUpdateRequestDto;

public interface UpdateReviewUseCase {

    Long updateReview(Long reviewId, ReviewUpdateRequestDto requestDto);
}
