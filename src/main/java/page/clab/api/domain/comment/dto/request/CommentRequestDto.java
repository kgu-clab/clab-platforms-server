package page.clab.api.domain.comment.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
public class CommentRequestDto {

    @NotNull(message = "{notNull.comment.content}")
    @Size(min = 1, max = 1000, message = "{size.comment.content}")
    @Schema(description = "내용", example = "댓글 내용", required = true)
    private String content;

    public static CommentGetAllResponseDto of(Comment comment) {
        return ModelMapperUtil.getModelMapper().map(comment, CommentGetAllResponseDto.class);
    }

}
