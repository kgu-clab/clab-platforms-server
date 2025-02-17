package page.clab.api.domain.members.blog.application.port.in;

import page.clab.api.domain.members.blog.application.dto.request.BlogUpdateRequestDto;

public interface UpdateBlogUseCase {

    Long updateBlog(Long blogId, BlogUpdateRequestDto requestDto);
}
