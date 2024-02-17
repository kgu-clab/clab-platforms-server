package page.clab.api.domain.comment.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
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
public class CommentUpdateRequestDto {

    @Size(min = 1, max = 1000, message = "{size.comment.content}")
    @Schema(description = "내용", example = "댓글 내용")
    private String content;

    @Schema(description = "익명 사용 여부", example = "false")
    private boolean wantAnonymous;

    public static CommentGetAllResponseDto of(Comment comment) {
        return ModelMapperUtil.getModelMapper().map(comment, CommentGetAllResponseDto.class);
    }

}
