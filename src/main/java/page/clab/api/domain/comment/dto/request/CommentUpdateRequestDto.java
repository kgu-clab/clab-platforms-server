package page.clab.api.domain.comment.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import page.clab.api.domain.comment.domain.Comment;
import page.clab.api.domain.comment.dto.response.CommentGetAllResponseDto;
import page.clab.api.global.util.ModelMapperUtil;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentUpdateRequestDto {

    @NotNull(message = "{notNull.comment.content}")
    @Schema(description = "내용", example = "댓글 내용")
    private String content;

    @NotNull(message = "{notNull.comment.wantAnonymous}")
    @Schema(description = "익명 사용 여부", example = "false")
    private boolean wantAnonymous;

    public static CommentGetAllResponseDto of(Comment comment) {
        return ModelMapperUtil.getModelMapper().map(comment, CommentGetAllResponseDto.class);
    }

}
