package page.clab.api.domain.blog.application;

import org.springframework.data.domain.Pageable;
import page.clab.api.domain.blog.dto.response.BlogDetailsResponseDto;
import page.clab.api.global.common.dto.PagedResponseDto;

public interface FetchDeletedBlogsService {
    PagedResponseDto<BlogDetailsResponseDto> execute(Pageable pageable);
}