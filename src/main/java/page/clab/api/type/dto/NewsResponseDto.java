package page.clab.api.type.dto;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import page.clab.api.type.entity.News;
import page.clab.api.util.ModelMapperUtil;

@Getter
@Setter
@ToString
public class NewsResponseDto {

    private Long id;
    
    private String category;

    private String title;

    private String subtitle;

    private String content;

    private String url;

    private LocalDateTime createdAt;
    
    private LocalDateTime updateTime;

    public static NewsResponseDto of(News news) {
        return ModelMapperUtil.getModelMapper().map(news, NewsResponseDto.class);
    }

}
