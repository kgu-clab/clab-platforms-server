package page.clab.api.domain.review.application.port.in;

import page.clab.api.global.exception.PermissionDeniedException;

public interface ReviewRemoveUseCase {
    Long remove(Long reviewId) throws PermissionDeniedException;
}
