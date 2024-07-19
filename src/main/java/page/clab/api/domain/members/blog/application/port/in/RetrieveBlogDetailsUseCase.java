package page.clab.api.domain.members.blog.application.port.in;

import page.clab.api.domain.members.blog.application.dto.response.BlogDetailsResponseDto;

public interface RetrieveBlogDetailsUseCase {
    BlogDetailsResponseDto retrieveBlogDetails(Long blogId);
}
