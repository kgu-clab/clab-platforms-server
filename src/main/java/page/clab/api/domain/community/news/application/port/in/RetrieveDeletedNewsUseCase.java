package page.clab.api.domain.community.news.application.port.in;

import org.springframework.data.domain.Pageable;
import page.clab.api.domain.community.news.application.dto.response.NewsDetailsResponseDto;
import page.clab.api.global.common.dto.PagedResponseDto;

public interface RetrieveDeletedNewsUseCase {
    PagedResponseDto<NewsDetailsResponseDto> retrieveDeletedNews(Pageable pageable);
}
