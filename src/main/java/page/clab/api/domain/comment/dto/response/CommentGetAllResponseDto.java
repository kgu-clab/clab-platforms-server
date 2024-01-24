package page.clab.api.domain.comment.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import page.clab.api.domain.comment.domain.Comment;
import page.clab.api.global.util.ModelMapperUtil;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class CommentGetAllResponseDto {

    private Long id;

    private String writerNickname;

    private String content;

    private List<CommentGetAllResponseDto> children;

    private LocalDateTime createdAt;

    public static CommentGetAllResponseDto of(Comment comment) {
        CommentGetAllResponseDto commentGetAllResponseDto = ModelMapperUtil.getModelMapper().map(comment, CommentGetAllResponseDto.class);
        commentGetAllResponseDto.setWriterNickname(comment.getNickname());
        List<CommentGetAllResponseDto> childrenDto = comment.getChildren().stream()
                .map(CommentGetAllResponseDto::of)
                .collect(Collectors.toList());
        commentGetAllResponseDto.setChildren(childrenDto);
        return commentGetAllResponseDto;
    }

}
