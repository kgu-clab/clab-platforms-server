package page.clab.api.domain.news.application.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.news.application.NewsDetailsRetrievalService;
import page.clab.api.domain.news.dao.NewsRepository;
import page.clab.api.domain.news.domain.News;
import page.clab.api.domain.news.dto.response.NewsDetailsResponseDto;
import page.clab.api.global.exception.NotFoundException;

@Service
@RequiredArgsConstructor
public class NewsDetailsRetrievalServiceImpl implements NewsDetailsRetrievalService {

    private final NewsRepository newsRepository;

    @Transactional(readOnly = true)
    @Override
    public NewsDetailsResponseDto retrieve(Long newsId) {
        News news = getNewsByIdOrThrow(newsId);
        return NewsDetailsResponseDto.toDto(news);
    }

    private News getNewsByIdOrThrow(Long newsId) {
        return newsRepository.findById(newsId)
                .orElseThrow(() -> new NotFoundException("해당 뉴스가 존재하지 않습니다."));
    }
}
