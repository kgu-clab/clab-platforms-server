package page.clab.api.domain.news.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.news.application.port.in.RetrieveNewsDetailsUseCase;
import page.clab.api.domain.news.application.port.out.LoadNewsPort;
import page.clab.api.domain.news.domain.News;
import page.clab.api.domain.news.dto.response.NewsDetailsResponseDto;

@Service
@RequiredArgsConstructor
public class NewsDetailsRetrievalService implements RetrieveNewsDetailsUseCase {

    private final LoadNewsPort loadNewsPort;

    @Transactional(readOnly = true)
    @Override
    public NewsDetailsResponseDto retrieve(Long newsId) {
        News news = loadNewsPort.findByIdOrThrow(newsId);
        return NewsDetailsResponseDto.toDto(news);
    }
}
