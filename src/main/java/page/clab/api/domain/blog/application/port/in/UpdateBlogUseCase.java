package page.clab.api.domain.blog.application.port.in;

import page.clab.api.domain.blog.application.dto.request.BlogUpdateRequestDto;
import page.clab.api.global.exception.PermissionDeniedException;

public interface UpdateBlogUseCase {
    Long updateBlog(Long blogId, BlogUpdateRequestDto requestDto) throws PermissionDeniedException;
}
