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

    @URL(message = "{url.news.url}")
    @Schema(description = "URL", example = "https://blog.naver.com/kyonggi_love/223199431495")
    private String imageUrl;

    public static NewsRequestDto of(News news) {
        return ModelMapperUtil.getModelMapper().map(news, NewsRequestDto.class);
    }

}
