package page.clab.api.domain.blog.application.port.in;

import page.clab.api.global.exception.PermissionDeniedException;

public interface BlogRemoveUseCase {
    Long remove(Long blogId) throws PermissionDeniedException;
}
