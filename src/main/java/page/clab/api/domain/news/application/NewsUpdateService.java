package page.clab.api.domain.news.application;

import page.clab.api.domain.news.dto.request.NewsUpdateRequestDto;

public interface NewsUpdateService {
    Long update(Long newsId, NewsUpdateRequestDto requestDto);
}
