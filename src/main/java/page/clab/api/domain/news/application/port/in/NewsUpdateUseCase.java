package page.clab.api.domain.news.application.port.in;

import page.clab.api.domain.news.dto.request.NewsUpdateRequestDto;

public interface NewsUpdateUseCase {
    Long update(Long newsId, NewsUpdateRequestDto requestDto);
}
