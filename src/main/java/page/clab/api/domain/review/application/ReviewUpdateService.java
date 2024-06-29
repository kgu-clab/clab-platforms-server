package page.clab.api.domain.review.application;

import page.clab.api.domain.review.dto.request.ReviewUpdateRequestDto;
import page.clab.api.global.exception.PermissionDeniedException;

public interface ReviewUpdateService {
    Long update(Long reviewId, ReviewUpdateRequestDto requestDto) throws PermissionDeniedException;
}
