package page.clab.api.domain.news.application;

import page.clab.api.domain.news.dto.request.NewsRequestDto;

public interface CreateNewsService {
    Long execute(NewsRequestDto requestDto);
}
