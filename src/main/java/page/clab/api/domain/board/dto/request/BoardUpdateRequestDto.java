package page.clab.api.domain.board.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import page.clab.api.domain.board.domain.BoardCategory;

@Getter
@Setter
public class BoardUpdateRequestDto {

    @Schema(description = "카테고리", example = "NOTICE")
    private BoardCategory category;

    @Schema(description = "제목", example = "2023년 2학기 모집 안내")
    private String title;

    @Schema(description = "내용", example = "2023년 2학기 모집 안내")
    private String content;

    @NotNull(message = "{notNull.board.wantAnonymous}")
    @Schema(description = "익명 사용 여부", example = "false")
    private boolean wantAnonymous;

}
