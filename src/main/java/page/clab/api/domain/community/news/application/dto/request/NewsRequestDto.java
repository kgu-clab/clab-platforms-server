package page.clab.api.domain.community.news.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import page.clab.api.domain.community.news.domain.News;

import java.time.LocalDate;

@Getter
@Setter
public class NewsRequestDto {

    @NotNull(message = "{notNull.news.title}")
    @Schema(description = "제목", example = "컴퓨터공학과, SW 개발보안 경진대회 최우수상, 우수상 수상", required = true)
    private String title;

    @NotNull(message = "{notNull.news.category}")
    @Schema(description = "카테고리", example = "동아리 소식", required = true)
    private String category;

    @NotNull(message = "{notNull.news.content}")
    @Schema(description = "내용", example = "컴퓨터공학과, SW 개발보안 경진대회 최우수상, 우수상 수상", required = true)
    private String content;

    @Schema(description = "URL", example = "https://blog.naver.com/kyonggi_love/223199431495", required = true)
    private String articleUrl;

    @NotNull(message = "{notNull.news.source}")
    @Schema(description = "출처", example = "경기대학교 공식 블로그", required = true)
    private String source;

    @NotNull(message = "{notNull.news.date}")
    @Schema(description = "날짜", example = "2021-08-31", required = true)
    private LocalDate date;

    public static News toEntity(NewsRequestDto requestDto) {
        return News.builder()
                .title(requestDto.getTitle())
                .category(requestDto.getCategory())
                .content(requestDto.getContent())
                .articleUrl(requestDto.getArticleUrl())
                .source(requestDto.getSource())
                .date(requestDto.getDate())
                .isDeleted(false)
                .build();
    }
}
