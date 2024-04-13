package page.clab.api.domain.board.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoardLikeRequestDto {

    @NotNull(message = "{notNull.boardLike.boardId}")
    @Schema(description = "게시글 아이디", example = "1", required = true)
    private Long boardId;

}
