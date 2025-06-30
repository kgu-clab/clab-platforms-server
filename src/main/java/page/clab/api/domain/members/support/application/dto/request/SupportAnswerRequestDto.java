package page.clab.api.domain.members.support.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SupportAnswerRequestDto {

    @NotNull(message = "{notNull.answer.content}")
    @Schema(description = "답변 내용", example = "버그를 확인 후 수정 완료했습니다.", required = true)
    private String content;
}
