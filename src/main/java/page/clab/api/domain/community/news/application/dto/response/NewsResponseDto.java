package page.clab.api.domain.community.news.application.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class NewsResponseDto {

    private Long id;
    private String title;
    private String category;
    private String articleUrl;
    private LocalDate date;
    private LocalDateTime createdAt;
}
