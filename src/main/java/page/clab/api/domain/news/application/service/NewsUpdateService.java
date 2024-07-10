package page.clab.api.domain.news.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.news.application.dto.request.NewsUpdateRequestDto;
import page.clab.api.domain.news.application.port.in.UpdateNewsUseCase;
import page.clab.api.domain.news.application.port.out.RetrieveNewsPort;
import page.clab.api.domain.news.application.port.out.UpdateNewsPort;
import page.clab.api.domain.news.domain.News;

@Service
@RequiredArgsConstructor
public class NewsUpdateService implements UpdateNewsUseCase {

    private final RetrieveNewsPort retrieveNewsPort;
    private final UpdateNewsPort updateNewsPort;

    @Transactional
    @Override
    public Long updateNews(Long newsId, NewsUpdateRequestDto requestDto) {
        News news = retrieveNewsPort.findByIdOrThrow(newsId);
        news.update(requestDto);
        return updateNewsPort.update(news).getId();
    }
}
