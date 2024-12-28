package page.clab.api.domain.community.news.application.port.in;

import page.clab.api.domain.community.news.application.dto.response.NewsDetailsResponseDto;

public interface RetrieveNewsDetailsUseCase {

    NewsDetailsResponseDto retrieveNewsDetails(Long newsId);
}
