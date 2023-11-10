package page.clab.api.type.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.URL;
import page.clab.api.type.entity.News;
import page.clab.api.util.ModelMapperUtil;

@Getter
@Setter
@ToString
public class NewsRequestDto {

    @NotNull(message = "{notNull.news.category}")
    @Size(min = 1, message = "{size.news.category}")
    private String category;

    @NotNull(message = "{notNull.news.title}")
    @Size(min = 1, max = 100, message = "{size.news.title}")
    private String title;

    @Size(max = 100)
    private String subtitle;

    @NotNull(message = "{notNull.news.content}")
    @Size(min = 1, message = "{size.news.content}")
    private String content;

    @URL(message = "{url.news.url}")
    private String url;

    public static NewsRequestDto of(News news) {
        return ModelMapperUtil.getModelMapper().map(news, NewsRequestDto.class);
    }

}
