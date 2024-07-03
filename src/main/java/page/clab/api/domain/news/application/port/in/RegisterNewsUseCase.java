package page.clab.api.domain.news.application.port.in;

import page.clab.api.domain.news.dto.request.NewsRequestDto;

public interface RegisterNewsUseCase {
    Long register(NewsRequestDto requestDto);
}
