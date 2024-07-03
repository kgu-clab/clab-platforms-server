package page.clab.api.domain.blog.application.port.in;

import page.clab.api.domain.blog.dto.response.BlogDetailsResponseDto;

public interface RetrieveBlogDetailsUseCase {
    BlogDetailsResponseDto retrieve(Long blogId);
}
