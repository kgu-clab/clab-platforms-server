package page.clab.api.type.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import page.clab.api.type.entity.Comment;
import page.clab.api.util.ModelMapperUtil;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentRequestDto {

    @NotNull(message = "{notNull.comment.content}")
    @Size(min = 1, max = 1000, message = "{size.comment.content}")
    private String content;

    public static CommentResponseDto of(Comment comment) {
        return ModelMapperUtil.getModelMapper().map(comment, CommentResponseDto.class);
    }

}
