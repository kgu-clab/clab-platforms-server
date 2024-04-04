package page.clab.api.domain.news.dto.response;

import lombok.Builder;
import lombok.Getter;
import page.clab.api.domain.news.domain.News;

import java.time.LocalDate;

@Getter
@Builder
public class NewsResponseDto {

    private Long id;

    private String title;

    private String category;

    private String articleUrl;

    private LocalDate date;

    public static NewsResponseDto toDto(News news) {
        return NewsResponseDto.builder()
                .id(news.getId())
                .title(news.getTitle())
                .category(news.getCategory())
                .articleUrl(news.getArticleUrl())
                .date(news.getDate())
                .build();
    }

}
