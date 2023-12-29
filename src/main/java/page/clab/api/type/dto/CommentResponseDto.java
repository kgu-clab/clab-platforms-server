package page.clab.api.type.dto;

import java.time.LocalDateTime;
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
public class CommentResponseDto {

    private Long id;

    private String writerName;

    private String writerImageUrl;

    private String content;

    private LocalDateTime createdAt;

    public static CommentResponseDto of(Comment comment) {
        CommentResponseDto commentResponseDto = ModelMapperUtil.getModelMapper().map(comment, CommentResponseDto.class);
        commentResponseDto.setWriterName(comment.getWriter().getName());
        commentResponseDto.setWriterImageUrl(comment.getWriter().getImageUrl());
        return commentResponseDto;
    }

}
