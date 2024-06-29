package page.clab.api.domain.blog.application;

import page.clab.api.domain.blog.dto.response.BlogDetailsResponseDto;

public interface FetchBlogDetailsService {
    BlogDetailsResponseDto execute(Long blogId);
}
