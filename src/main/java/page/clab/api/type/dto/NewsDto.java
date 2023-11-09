package page.clab.api.type.dto;

import java.time.LocalDateTime;
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
public class NewsDto {

    @NotNull
    private String category;

    @NotNull
    @Size(min = 1, max = 100)
    private String title;

    @Size(max = 100)
    private String subtitle;

    @NotNull
    private String content;

    @URL
    private String url;

    @NotNull
    private LocalDateTime createdAt;

    private LocalDateTime updateTime;

    public static NewsDto of(News news){
        return ModelMapperUtil.getModelMapper().map(news, NewsDto.class);
    }

}
