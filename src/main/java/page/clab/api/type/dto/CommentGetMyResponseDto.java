package page.clab.api.type.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import page.clab.api.type.entity.Comment;
import page.clab.api.util.ModelMapperUtil;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class CommentGetMyResponseDto {

    private Long id;

    private String writerName;

    private String writerImageUrl;

    private String content;

    private LocalDateTime createdAt;

    public static CommentGetMyResponseDto of(Comment comment) {
        CommentGetMyResponseDto commentGetAllResponseDto = ModelMapperUtil.getModelMapper().map(comment, CommentGetMyResponseDto.class);
        commentGetAllResponseDto.setWriterName(comment.getWriter().getName());
        commentGetAllResponseDto.setWriterImageUrl(comment.getWriter().getImageUrl());
        return commentGetAllResponseDto;
    }

}
