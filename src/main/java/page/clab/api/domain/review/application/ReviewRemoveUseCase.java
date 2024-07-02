package page.clab.api.domain.review.application;

import page.clab.api.global.exception.PermissionDeniedException;

public interface ReviewRemoveUseCase {
    Long remove(Long reviewId) throws PermissionDeniedException;
}
