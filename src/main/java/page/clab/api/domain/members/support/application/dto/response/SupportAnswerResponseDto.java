package page.clab.api.domain.members.support.application.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class SupportAnswerResponseDto {

    private String content;
    private String responder;
    private LocalDateTime createdAt;
}
