package page.clab.api.domain.community.news.application.port.in;

import page.clab.api.domain.community.news.application.dto.request.NewsRequestDto;

public interface RegisterNewsUseCase {
    Long registerNews(NewsRequestDto requestDto);
}
