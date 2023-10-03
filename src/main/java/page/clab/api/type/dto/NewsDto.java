package page.clab.api.type.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import page.clab.api.type.entity.News;
import page.clab.api.util.ModelMapperUtil;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class NewsDto {

    private String category;

    private String title;

    private String subtitle;

    private String content;

    private String url;

    private LocalDateTime createdAt;

    private LocalDateTime updateTime;

    public static NewsDto of(News News){
        return ModelMapperUtil.getModelMapper().map(News, NewsDto.class);
    }

}
