package page.clab.api.domain.news.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.news.application.port.in.RetrieveNewsDetailsUseCase;
import page.clab.api.domain.news.application.port.out.RetrieveNewsPort;
import page.clab.api.domain.news.domain.News;
import page.clab.api.domain.news.application.dto.response.NewsDetailsResponseDto;

@Service
@RequiredArgsConstructor
public class NewsDetailsRetrievalService implements RetrieveNewsDetailsUseCase {

    private final RetrieveNewsPort retrieveNewsPort;

    @Transactional(readOnly = true)
    @Override
    public NewsDetailsResponseDto retrieveNewsDetails(Long newsId) {
        News news = retrieveNewsPort.findByIdOrThrow(newsId);
        return NewsDetailsResponseDto.toDto(news);
    }
}
