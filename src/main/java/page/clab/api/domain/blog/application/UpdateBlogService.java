package page.clab.api.domain.blog.application;

import page.clab.api.domain.blog.dto.request.BlogUpdateRequestDto;
import page.clab.api.global.exception.PermissionDeniedException;

public interface UpdateBlogService {
    Long execute(Long blogId, BlogUpdateRequestDto requestDto) throws PermissionDeniedException;
}