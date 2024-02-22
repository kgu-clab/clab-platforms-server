package page.clab.api.domain.news.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.URL;
import page.clab.api.domain.news.domain.News;
import page.clab.api.global.util.ModelMapperUtil;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@ToString
public class NewsRequestDto {

    @NotNull(message = "{notNull.news.title}")
    @Size(min = 1, max = 100, message = "{size.news.title}")
    @Schema(description = "제목", example = "컴퓨터공학과, SW 개발보안 경진대회 최우수상, 우수상 수상", required = true)
    private String title;

    @NotNull(message = "{notNull.news.category}")
    @Size(min = 1, message = "{size.news.category}")
    @Schema(description = "카테고리", example = "동아리 소식", required = true)
    private String category;

    @NotNull(message = "{notNull.news.content}")
    @Size(min = 1, max = 10000, message = "{size.news.content}")
    @Schema(description = "내용", example = "컴퓨터공학과, SW 개발보안 경진대회 최우수상, 우수상 수상", required = true)
    private String content;

    @URL(message = "{url.news.articleUrl}")
    @Schema(description = "URL", example = "https://blog.naver.com/kyonggi_love/223199431495", required = true)
    private String articleUrl;

    @NotNull(message = "{notNull.news.source}")
    @Schema(description = "출처", example = "경기대학교 공식 블로그", required = true)
    private String source;

    @Schema(description = "첨부파일 경로 리스트", example = "[\"/resources/files/news/339609571877700_4305d83e-090a-480b-a470-b5e96164d113.png\", \"/resources/files/news/4305d83e-090a-480b-a470-b5e96164d114.png\"]")
    private List<String> fileUrlList;

    @NotNull(message = "{notNull.news.date}")
    @Schema(description = "날짜", example = "2021-08-31", required = true)
    private LocalDate date;

    public static NewsRequestDto of(News news) {
        return ModelMapperUtil.getModelMapper().map(news, NewsRequestDto.class);
    }

}
