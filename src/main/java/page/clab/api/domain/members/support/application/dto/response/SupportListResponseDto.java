package page.clab.api.domain.members.support.application.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class SupportListResponseDto {

    private Long id;
    private String writerId;
    private String name;
    private String title;
    private LocalDateTime createdAt;
    private String status;
    private String category;
}
