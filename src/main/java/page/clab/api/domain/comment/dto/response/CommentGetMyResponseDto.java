package page.clab.api.domain.comment.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import page.clab.api.domain.comment.domain.Comment;
import page.clab.api.global.util.ModelMapperUtil;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class CommentGetMyResponseDto {

    private Long id;

    private Long boardId;

    private String boardCategory;

    private String writer;

    private String writerImageUrl;

    private String content;

    private Long likes;

    private boolean hasLikeByMe;

    private LocalDateTime createdAt;

    public static CommentGetMyResponseDto of(Comment comment) {
        CommentGetMyResponseDto commentGetAllResponseDto = ModelMapperUtil.getModelMapper().map(comment, CommentGetMyResponseDto.class);

        commentGetAllResponseDto.setBoardId(comment.getBoard().getId());
        commentGetAllResponseDto.setBoardCategory(comment.getBoard().getCategory());

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
