package page.clab.api.domain.news.dto.response;

import lombok.Builder;
import lombok.Getter;
import page.clab.api.domain.news.domain.News;
import page.clab.api.global.common.file.dto.response.UploadedFileResponseDto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class NewsDetailsResponseDto {

    private Long id;

    private String title;

    private String category;

    private String content;

    private String articleUrl;

    private String source;

    private List<UploadedFileResponseDto> files;

    private LocalDate date;

    private LocalDateTime createdAt;

    public static NewsDetailsResponseDto toDto(News news) {
        return NewsDetailsResponseDto.builder()
                .id(news.getId())
                .title(news.getTitle())
                .category(news.getCategory())
                .content(news.getContent())
                .articleUrl(news.getArticleUrl())
                .source(news.getSource())
                .files(UploadedFileResponseDto.toDto(news.getUploadedFiles()))
                .date(news.getDate())
                .createdAt(news.getCreatedAt())
                .build();
    }
}
