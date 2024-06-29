package page.clab.api.domain.review.application;

import page.clab.api.domain.review.dto.request.ReviewRequestDto;

public interface ReviewRegisterService {
    Long register(ReviewRequestDto requestDto);
}
