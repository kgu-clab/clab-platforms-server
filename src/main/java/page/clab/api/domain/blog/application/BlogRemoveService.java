package page.clab.api.domain.blog.application;

import page.clab.api.global.exception.PermissionDeniedException;

public interface BlogRemoveService {
    Long remove(Long blogId) throws PermissionDeniedException;
}
