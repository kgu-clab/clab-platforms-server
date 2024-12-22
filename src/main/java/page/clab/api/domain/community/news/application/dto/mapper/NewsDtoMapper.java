package page.clab.api.domain.community.news.application.dto.mapper;

import org.springframework.stereotype.Component;
import page.clab.api.domain.community.news.application.dto.response.NewsDetailsResponseDto;
import page.clab.api.domain.community.news.application.dto.response.NewsResponseDto;
import page.clab.api.domain.community.news.domain.News;

@Component
public class NewsDtoMapper {

    public NewsResponseDto toDto(News news) {
        return NewsResponseDto.builder()
            .id(news.getId())
            .title(news.getTitle())
            .category(news.getCategory())
            .articleUrl(news.getArticleUrl())
            .date(news.getDate())
            .createdAt(news.getCreatedAt())
            .build();
    }

    public NewsDetailsResponseDto toDetailsDto(News news) {
        return NewsDetailsResponseDto.builder()
            .id(news.getId())
            .title(news.getTitle())
            .category(news.getCategory())
            .content(news.getContent())
            .articleUrl(news.getArticleUrl())
            .source(news.getSource())
            .date(news.getDate())
            .createdAt(news.getCreatedAt())
            .build();
    }
}
