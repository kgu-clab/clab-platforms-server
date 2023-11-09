package page.clab.api.type.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import page.clab.api.type.entity.Comment;
import page.clab.api.util.ModelMapperUtil;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentResponseDto {

    private String content;

    private LocalDateTime updateTime;

    private String writerId;


    public static CommentResponseDto of(Comment comment) {
        return ModelMapperUtil.getModelMapper().map(comment, CommentResponseDto.class);
    }
}
