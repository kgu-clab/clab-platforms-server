package page.clab.api.domain.news.application.port.in;

import page.clab.api.domain.news.dto.request.NewsUpdateRequestDto;

public interface UpdateNewsUseCase {
    Long update(Long newsId, NewsUpdateRequestDto requestDto);
}
