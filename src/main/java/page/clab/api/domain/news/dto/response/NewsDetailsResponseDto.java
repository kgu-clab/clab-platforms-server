package page.clab.api.domain.news.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import page.clab.api.domain.news.domain.News;
import page.clab.api.global.common.file.dto.response.UploadedFileResponseDto;
import page.clab.api.global.util.ModelMapperUtil;

@Getter
@Setter
@ToString
public class NewsDetailsResponseDto {

    private Long id;

    private String title;

    private String category;

    private String content;

    private String articleUrl;

    private String source;

    private List<UploadedFileResponseDto> files = new ArrayList<>();

    private LocalDate date;

    private LocalDateTime createdAt;

    public static NewsDetailsResponseDto of(News news) {
        return ModelMapperUtil.getModelMapper().map(news, NewsDetailsResponseDto.class);
    }

}
