package page.clab.api.domain.comment.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentLikeRequestDto {

    @NotNull(message = "{notNull.commentLike.commentId}")
    @Schema(description = "댓글 아이디", example = "1", required = true)
    private Long commentId;
}
