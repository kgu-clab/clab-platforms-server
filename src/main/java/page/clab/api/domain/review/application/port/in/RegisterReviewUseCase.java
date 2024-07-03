package page.clab.api.domain.review.application.port.in;

import page.clab.api.domain.review.dto.request.ReviewRequestDto;

public interface RegisterReviewUseCase {
    Long register(ReviewRequestDto requestDto);
}
