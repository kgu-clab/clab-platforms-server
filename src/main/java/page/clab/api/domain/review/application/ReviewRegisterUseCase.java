package page.clab.api.domain.review.application;

import page.clab.api.domain.review.dto.request.ReviewRequestDto;

public interface ReviewRegisterUseCase {
    Long register(ReviewRequestDto requestDto);
}
