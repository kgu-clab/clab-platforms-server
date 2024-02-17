package page.clab.api.domain.news.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.URL;
import page.clab.api.domain.news.domain.News;
import page.clab.api.global.util.ModelMapperUtil;

import java.time.LocalDate;

@Getter
@Setter
public class NewsUpdateRequestDto {

    @Size(min = 1, max = 100, message = "{size.news.title}")
    @Schema(description = "제목", example = "컴퓨터공학과, SW 개발보안 경진대회 최우수상, 우수상 수상")
    private String title;

    @Size(min = 1, message = "{size.news.category}")
    @Schema(description = "카테고리", example = "동아리 소식")
    private String category;

    @Size(min = 1, max = 10000, message = "{size.news.content}")
    @Schema(description = "내용", example = "컴퓨터공학과, SW 개발보안 경진대회 최우수상, 우수상 수상")
    private String content;

    @URL(message = "{url.news.articleUrl}")
    @Schema(description = "URL", example = "https://blog.naver.com/kyonggi_love/223199431495")
    private String articleUrl;

    @Schema(description = "출처", example = "경기대학교 공식 블로그")
    private String source;

    @Schema(description = "날짜", example = "2021-08-31")
    private LocalDate date;

    public static NewsUpdateRequestDto of(News news) {
        return ModelMapperUtil.getModelMapper().map(news, NewsUpdateRequestDto.class);
    }

}
