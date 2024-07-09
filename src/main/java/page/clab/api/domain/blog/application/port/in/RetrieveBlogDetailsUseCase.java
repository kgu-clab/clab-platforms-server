package page.clab.api.domain.blog.application.port.in;

import page.clab.api.domain.blog.application.dto.response.BlogDetailsResponseDto;

public interface RetrieveBlogDetailsUseCase {
    BlogDetailsResponseDto retrieveBlogDetails(Long blogId);
}
