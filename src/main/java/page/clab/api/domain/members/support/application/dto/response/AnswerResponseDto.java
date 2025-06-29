package page.clab.api.domain.members.support.application.dto.response;

import lombok.Builder;
import lombok.Getter;
import page.clab.api.domain.members.support.domain.Answer;

import java.time.LocalDateTime;

@Getter
@Builder
public class AnswerResponseDto {

    private String content;
    private String responder;
    private LocalDateTime createdAt;

    public static AnswerResponseDto from(Answer answer) {
        return AnswerResponseDto.builder()
                .content(answer.getContent())
                .responder(answer.getAdminName())
                .createdAt(answer.getCreatedAt())
                .build();
    }
}
