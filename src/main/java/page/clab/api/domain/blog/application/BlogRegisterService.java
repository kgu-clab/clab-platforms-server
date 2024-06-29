package page.clab.api.domain.blog.application;

import page.clab.api.domain.blog.dto.request.BlogRequestDto;

public interface BlogRegisterService {
    Long register(BlogRequestDto requestDto);
}