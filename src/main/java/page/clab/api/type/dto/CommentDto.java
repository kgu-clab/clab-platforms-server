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
public class CommentDto {

    private String content;

    private LocalDateTime updateTime;

    private String writer_id;


    public static CommentDto of(Comment comment) {
        return ModelMapperUtil.getModelMapper().map(comment, CommentDto.class);
    }
}
