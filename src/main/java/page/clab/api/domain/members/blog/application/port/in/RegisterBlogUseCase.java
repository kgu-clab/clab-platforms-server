package page.clab.api.domain.members.blog.application.port.in;

import page.clab.api.domain.members.blog.application.dto.request.BlogRequestDto;

public interface RegisterBlogUseCase {

    Long registerBlog(BlogRequestDto requestDto);
}
