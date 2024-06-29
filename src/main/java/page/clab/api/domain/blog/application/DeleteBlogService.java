package page.clab.api.domain.blog.application;

import page.clab.api.global.exception.PermissionDeniedException;

public interface DeleteBlogService {
    Long execute(Long blogId) throws PermissionDeniedException;
}
