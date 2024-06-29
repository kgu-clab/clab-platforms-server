package page.clab.api.domain.news.application;

import page.clab.api.domain.news.dto.request.NewsRequestDto;

public interface NewsRegisterService {
    Long register(NewsRequestDto requestDto);
}
