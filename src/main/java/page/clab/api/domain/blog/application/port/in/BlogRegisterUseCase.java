package page.clab.api.domain.blog.application.port.in;

import page.clab.api.domain.blog.dto.request.BlogRequestDto;

public interface BlogRegisterUseCase {
    Long register(BlogRequestDto requestDto);
}