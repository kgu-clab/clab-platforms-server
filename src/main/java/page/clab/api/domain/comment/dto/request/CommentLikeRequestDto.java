package page.clab.api.domain.comment.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentLikeRequestDto {

    @NotNull(message = "{notNull.board.category}")
    @Schema(description = "댓글 아이디", example = "1", required = true)
    private Long commentId;

}
