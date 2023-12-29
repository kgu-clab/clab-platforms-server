package page.clab.api.type.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import page.clab.api.type.entity.Comment;
import page.clab.api.util.ModelMapperUtil;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentRequestDto {

    @NotNull(message = "{notNull.comment.content}")
    @Size(min = 1, max = 1000, message = "{size.comment.content}")
    @Schema(description = "내용", example = "댓글 내용", required = true)
    private String content;

    public static CommentResponseDto of(Comment comment) {
        return ModelMapperUtil.getModelMapper().map(comment, CommentResponseDto.class);
    }

}
