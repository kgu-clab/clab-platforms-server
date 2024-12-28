package page.clab.api.domain.community.news.application.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import page.clab.api.global.common.file.dto.response.UploadedFileResponseDto;

@Getter
@Builder
public class NewsDetailsResponseDto {

    private Long id;
    private String title;
    private String category;
    private String content;
    private String articleUrl;
    private String source;
    private List<UploadedFileResponseDto> files;
    private LocalDate date;
    private LocalDateTime createdAt;
}
