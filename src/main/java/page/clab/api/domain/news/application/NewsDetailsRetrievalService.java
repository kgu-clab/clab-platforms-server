package page.clab.api.domain.news.application;

import page.clab.api.domain.news.dto.response.NewsDetailsResponseDto;

public interface NewsDetailsRetrievalService {
    NewsDetailsResponseDto retrieve(Long newsId);
}
