package page.clab.api.domain.community.news.application.port.in;

import page.clab.api.domain.community.news.application.dto.request.NewsUpdateRequestDto;

public interface UpdateNewsUseCase {
    Long updateNews(Long newsId, NewsUpdateRequestDto requestDto);
}
