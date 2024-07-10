package page.clab.api.domain.news.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.news.application.dto.request.NewsRequestDto;
import page.clab.api.domain.news.application.port.in.RegisterNewsUseCase;
import page.clab.api.domain.news.application.port.out.RegisterNewsPort;
import page.clab.api.domain.news.domain.News;

@Service
@RequiredArgsConstructor
public class NewsRegisterService implements RegisterNewsUseCase {

    private final RegisterNewsPort registerNewsPort;

    @Transactional
    @Override
    public Long registerNews(NewsRequestDto requestDto) {
        News news = NewsRequestDto.toEntity(requestDto);
        return registerNewsPort.save(news).getId();
    }
}
