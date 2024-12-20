package page.clab.api.domain.community.comment.application.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CommentLikeToggleResponseDto {

    private Long boardId;
    private Long likes;
    private Boolean isDeleted;
}
