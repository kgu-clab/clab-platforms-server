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
public class NewsDetailsResponseDto {

    private Long id;

    private String title;

    private String category;

    private String content;

    private String imageUrl;

    private LocalDateTime createdAt;

    public static NewsDetailsResponseDto of(News news) {
        return ModelMapperUtil.getModelMapper().map(news, NewsDetailsResponseDto.class);
    }

}
