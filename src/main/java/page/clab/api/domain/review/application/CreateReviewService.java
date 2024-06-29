package page.clab.api.domain.review.application;

import page.clab.api.domain.review.dto.request.ReviewRequestDto;

public interface CreateReviewService {
    Long execute(ReviewRequestDto requestDto);
}
