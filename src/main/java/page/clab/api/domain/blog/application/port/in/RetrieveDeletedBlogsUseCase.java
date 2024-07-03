package page.clab.api.domain.blog.application.port.in;

import org.springframework.data.domain.Pageable;
import page.clab.api.domain.blog.dto.response.BlogDetailsResponseDto;
import page.clab.api.global.common.dto.PagedResponseDto;

public interface RetrieveDeletedBlogsUseCase {
    PagedResponseDto<BlogDetailsResponseDto> retrieve(Pageable pageable);
}
