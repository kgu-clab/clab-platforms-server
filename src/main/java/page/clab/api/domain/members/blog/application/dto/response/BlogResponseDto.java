package page.clab.api.domain.members.blog.application.dto.response;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BlogResponseDto {

    private Long id;
    private String title;
    private String subTitle;
    private String imageUrl;
    private String hyperlink;
    private LocalDateTime createdAt;
}
