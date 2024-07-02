package page.clab.api.domain.review.application.port.in;

import page.clab.api.domain.review.dto.request.ReviewUpdateRequestDto;
import page.clab.api.global.exception.PermissionDeniedException;

public interface ReviewUpdateUseCase {
    Long update(Long reviewId, ReviewUpdateRequestDto requestDto) throws PermissionDeniedException;
}
