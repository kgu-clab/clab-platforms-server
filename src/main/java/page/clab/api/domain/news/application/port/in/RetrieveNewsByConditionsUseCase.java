package page.clab.api.domain.news.application.port.in;

import org.springframework.data.domain.Pageable;
import page.clab.api.domain.news.application.dto.response.NewsResponseDto;
import page.clab.api.global.common.dto.PagedResponseDto;

public interface RetrieveNewsByConditionsUseCase {
    PagedResponseDto<NewsResponseDto> retrieveNews(String title, String category, Pageable pageable);
}
