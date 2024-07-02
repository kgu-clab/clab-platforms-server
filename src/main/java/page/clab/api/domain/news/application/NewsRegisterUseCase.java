package page.clab.api.domain.news.application;

import page.clab.api.domain.news.dto.request.NewsRequestDto;

public interface NewsRegisterUseCase {
    Long register(NewsRequestDto requestDto);
}
