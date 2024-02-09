package page.clab.api.domain.comment.dto.response;

import java.time.LocalDateTime;
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
public class CommentGetMyResponseDto {

    private Long id;

    private String writer;

    private String writerImageUrl;

    private String content;

    private LocalDateTime createdAt;

    public static CommentGetMyResponseDto of(Comment comment) {
        CommentGetMyResponseDto commentGetAllResponseDto = ModelMapperUtil.getModelMapper().map(comment, CommentGetMyResponseDto.class);

        if(comment.isWantAnonymous()){
            commentGetAllResponseDto.setWriter(comment.getNickname());
            commentGetAllResponseDto.setWriter(null);
        }
        else{
            commentGetAllResponseDto.setWriter(comment.getWriter().getName());
            commentGetAllResponseDto.setWriter(comment.getWriter().getImageUrl());
        }

        commentGetAllResponseDto.setWriterImageUrl(comment.getWriter().getImageUrl());

        return commentGetAllResponseDto;
    }

}
