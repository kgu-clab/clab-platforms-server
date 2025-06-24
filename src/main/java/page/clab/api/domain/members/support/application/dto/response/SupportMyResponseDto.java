package page.clab.api.domain.members.support.application.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class SupportMyResponseDto {

    private Long id;
    private String title;
    private String name;
    private LocalDateTime createdAt;
    private String status;
    private String category;
}
