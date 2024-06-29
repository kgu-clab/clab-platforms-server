package page.clab.api.domain.review.application;

import page.clab.api.domain.review.dto.request.ReviewUpdateRequestDto;
import page.clab.api.global.exception.PermissionDeniedException;

public interface UpdateReviewService {
    Long execute(Long reviewId, ReviewUpdateRequestDto requestDto) throws PermissionDeniedException;
}
