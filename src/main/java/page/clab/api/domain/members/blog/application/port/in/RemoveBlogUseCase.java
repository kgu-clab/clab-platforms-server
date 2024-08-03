package page.clab.api.domain.members.blog.application.port.in;

import page.clab.api.global.exception.PermissionDeniedException;

public interface RemoveBlogUseCase {
    Long removeBlog(Long blogId) throws PermissionDeniedException;
}
