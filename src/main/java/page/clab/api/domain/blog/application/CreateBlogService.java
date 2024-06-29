package page.clab.api.domain.blog.application;

import page.clab.api.domain.blog.dto.request.BlogRequestDto;

public interface CreateBlogService {
    Long execute(BlogRequestDto requestDto);
}